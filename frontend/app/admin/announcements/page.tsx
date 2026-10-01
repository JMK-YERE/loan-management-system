'use client';

import { useState, useEffect } from 'react';
import { Plus, Edit, Trash2, Eye, EyeOff, Save, X } from 'lucide-react';
import { announcementAPI } from '@/lib/api';
import { useAuth } from '@/store/auth';

interface Announcement {
  id?: number;
  title: string;
  content: string;
  tag: string;
  color: string;
  emoji: string;
  ctaText: string;
  ctaLink: string;
  imageUrl: string;
  active: boolean;
  displayOrder: number;
}

const emptyForm: Announcement = {
  title: '',
  content: '',
  tag: 'Habari',
  color: 'from-blue-500 to-indigo-600',
  emoji: '📢',
  ctaText: '',
  ctaLink: '',
  imageUrl: '',
  active: true,
  displayOrder: 0,
};

const colorOptions = [
  { label: 'Bluu', value: 'from-blue-500 to-indigo-600' },
  { label: 'Kijani', value: 'from-green-500 to-emerald-600' },
  { label: 'Chungwa', value: 'from-orange-500 to-red-500' },
  { label: 'Zambarau', value: 'from-purple-500 to-pink-600' },
  { label: 'Cyan', value: 'from-cyan-500 to-blue-600' },
  { label: 'Dhahabu', value: 'from-yellow-500 to-orange-500' },
];

const emojiOptions = ['📢', '💰', '📱', '🛡️', '💬', '🚀', '🔥', '🎉', '⭐', '💳', '📊', '🤝'];

export default function AnnouncementsPage() {
  const { token } = useAuth();
  const [announcements, setAnnouncements] = useState<Announcement[]>([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [form, setForm] = useState<Announcement>(emptyForm);
  const [saving, setSaving] = useState(false);

  const load = async () => {
    try {
      const res = await announcementAPI.getAll();
      setAnnouncements(res.data.data || []);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const handleSubmit = async () => {
    if (!form.title || !form.content) {
      alert('Jaza kichwa na maudhui');
      return;
    }
    setSaving(true);
    try {
      if (editingId) {
        await announcementAPI.update(editingId, form);
      } else {
        await announcementAPI.create(form);
      }
      setShowForm(false);
      setEditingId(null);
      setForm(emptyForm);
      await load();
    } catch (e: any) {
      alert(e.response?.data?.message || 'Kosa');
    } finally {
      setSaving(false);
    }
  };

  const handleEdit = (a: Announcement) => {
    setForm(a);
    setEditingId(a.id!);
    setShowForm(true);
  };

  const handleDelete = async (id: number) => {
    if (!confirm('Una uhakika kufuta tangazo hili?')) return;
    try {
      await announcementAPI.delete(id);
      await load();
    } catch (e: any) {
      alert(e.response?.data?.message || 'Kosa');
    }
  };

  const handleToggle = async (a: Announcement) => {
    try {
      await announcementAPI.update(a.id!, { ...a, active: !a.active });
      await load();
    } catch (e: any) {
      alert('Kosa');
    }
  };

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">Matangazo</h1>
          <p className="text-gray-600 dark:text-gray-400">Simamia matangazo yanayoonekana kwenye tovuti</p>
        </div>
        <button onClick={() => { setForm(emptyForm); setEditingId(null); setShowForm(true); }} className="inline-flex items-center gap-2 px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 text-white font-bold rounded-xl hover:from-blue-700 hover:to-indigo-700 transition shadow-lg">
          <Plus className="w-5 h-5" />
          Tangazo Jipya
        </button>
      </div>

      {loading ? (
        <div className="text-center py-12 text-gray-500">Inapakia...</div>
      ) : announcements.length === 0 ? (
        <div className="bg-white dark:bg-gray-900 rounded-2xl p-12 border border-gray-200 dark:border-gray-800 text-center">
          <p className="text-gray-500 mb-4">Hakuna matangazo bado.</p>
          <button onClick={() => setShowForm(true)} className="text-blue-600 font-semibold hover:underline">
            Unda tangazo la kwanza
          </button>
        </div>
      ) : (
        <div className="grid gap-4">
          {announcements.map((a) => (
            <div key={a.id} className="bg-white dark:bg-gray-900 rounded-2xl p-6 border border-gray-200 dark:border-gray-800 flex items-center justify-between gap-4">
              <div className="flex items-center gap-4 flex-1 min-w-0">
                <div className={`w-14 h-14 rounded-xl bg-gradient-to-br ${a.color} flex items-center justify-center text-2xl shadow-lg flex-shrink-0`}>
                  {a.emoji}
                </div>
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-1">
                    <span className="px-2 py-0.5 rounded-full bg-blue-100 dark:bg-blue-950 text-blue-700 dark:text-blue-300 text-xs font-bold">{a.tag}</span>
                    {!a.active && <span className="px-2 py-0.5 rounded-full bg-red-100 dark:bg-red-950 text-red-700 dark:text-red-300 text-xs font-bold">IMEZIMWA</span>}
                  </div>
                  <h3 className="font-bold text-gray-900 dark:text-white truncate">{a.title}</h3>
                  <p className="text-sm text-gray-600 dark:text-gray-400 truncate">{a.content}</p>
                </div>
              </div>
              <div className="flex items-center gap-2 flex-shrink-0">
                <button onClick={async () => { try { const res=await announcementAPI.broadcastBorrowers(a.id!); alert('Sent: ' + (res.data?.data ?? 0)); } catch (e:any) { alert(e.response?.data?.message || 'Failed'); } }} className="rounded-lg px-3 py-2 text-xs font-bold text-blue-600">Notify</button>
                <button onClick={() => handleToggle(a)} className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800 transition" title={a.active ? 'Zima' : 'Washa'}>
                  {a.active ? <Eye className="w-5 h-5 text-green-600" /> : <EyeOff className="w-5 h-5 text-gray-400" />}
                </button>
                <button onClick={() => handleEdit(a)} className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800 transition" title="Badilisha">
                  <Edit className="w-5 h-5 text-blue-600" />
                </button>
                <button onClick={() => handleDelete(a.id!)} className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800 transition" title="Futa">
                  <Trash2 className="w-5 h-5 text-red-600" />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Modal */}
      {showForm && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4 overflow-auto">
          <div className="bg-white dark:bg-gray-900 rounded-2xl p-8 max-w-2xl w-full my-8">
            <div className="flex justify-between items-center mb-6">
              <h2 className="text-2xl font-bold text-gray-900 dark:text-white">
                {editingId ? 'Badilisha Tangazo' : 'Tangazo Jipya'}
              </h2>
              <button onClick={() => { setShowForm(false); setEditingId(null); }} className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800">
                <X className="w-6 h-6" />
              </button>
            </div>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Kichwa *</label>
                <input value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent" placeholder="Riba 5% kwa Wiki Hii" />
              </div>

              <div>
                <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Maudhui *</label>
                <textarea value={form.content} onChange={(e) => setForm({ ...form, content: e.target.value })} rows={3} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500 focus:border-transparent" placeholder="Eleza tangazo lako..." />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Tag</label>
                  <input value={form.tag} onChange={(e) => setForm({ ...form, tag: e.target.value })} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white" placeholder="Ofa" />
                </div>
                <div>
                  <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Emoji</label>
                  <select value={form.emoji} onChange={(e) => setForm({ ...form, emoji: e.target.value })} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white">
                    {emojiOptions.map((e) => <option key={e} value={e}>{e}</option>)}
                  </select>
                </div>
              </div>

              <div>
                <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-2">Rangi</label>
                <div className="grid grid-cols-3 gap-2">
                  {colorOptions.map((c) => (
                    <button key={c.value} type="button" onClick={() => setForm({ ...form, color: c.value })} className={`h-12 rounded-xl bg-gradient-to-br ${c.value} text-white text-xs font-bold border-2 ${form.color === c.value ? 'border-gray-900 dark:border-white' : 'border-transparent'}`}>
                      {c.label}
                    </button>
                  ))}
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Kitufe (CTA)</label>
                  <input value={form.ctaText} onChange={(e) => setForm({ ...form, ctaText: e.target.value })} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white" placeholder="Jisajili" />
                </div>
                <div>
                  <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Kiungo (Link)</label>
                  <input value={form.ctaLink} onChange={(e) => setForm({ ...form, ctaLink: e.target.value })} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white" placeholder="/register" />
                </div>
              </div>

              <div>
                <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Picha ya tangazo (Image URL)</label>
                <input value={form.imageUrl} onChange={(e) => setForm({ ...form, imageUrl: e.target.value })} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white" placeholder="https://..." />
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Mpangilio (Order)</label>
                  <input type="number" value={form.displayOrder} onChange={(e) => setForm({ ...form, displayOrder: parseInt(e.target.value) || 0 })} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white" />
                </div>
                <div className="flex items-center gap-3 mt-7">
                  <input type="checkbox" id="active" checked={form.active} onChange={(e) => setForm({ ...form, active: e.target.checked })} className="w-5 h-5" />
                  <label htmlFor="active" className="text-sm font-semibold text-gray-700 dark:text-gray-300">Washa (Active)</label>
                </div>
              </div>
            </div>

            <div className="flex gap-3 mt-8">
              <button onClick={() => { setShowForm(false); setEditingId(null); }} className="flex-1 px-6 py-3 border-2 border-gray-300 dark:border-gray-700 text-gray-700 dark:text-gray-300 font-bold rounded-xl hover:bg-gray-50 dark:hover:bg-gray-800 transition">
                Ghairi
              </button>
              <button onClick={handleSubmit} disabled={saving} className="flex-1 inline-flex items-center justify-center gap-2 px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 text-white font-bold rounded-xl hover:from-blue-700 hover:to-indigo-700 transition shadow-lg disabled:opacity-50">
                <Save className="w-5 h-5" />
                {saving ? 'Inahifadhi...' : 'Hifadhi'}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
