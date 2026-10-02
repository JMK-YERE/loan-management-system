'use client';

import { useEffect, useState } from 'react';
import { adminAPI, adminUserAPI } from '@/lib/api';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import { useLanguage } from '@/lib/useLanguage';

const STATUS: Record<string, string> = {
  PENDING: 'bg-yellow-100 text-yellow-800',
  APPROVED: 'bg-green-100 text-green-800',
  REJECTED: 'bg-red-100 text-red-800',
};

const ROWS: [string, string][] = [
  ['Barua pepe', 'email'], ['Simu', 'phone'], ['Kitambulisho', 'idType'], ['Namba ya kitambulisho', 'idNumber'],
  ['Tarehe ya kuzaliwa', 'dateOfBirth'], ['Jinsia', 'gender'], ['Hali ya ndoa', 'maritalStatus'], ['Utaifa', 'nationality'],
  ['Anwani', 'address'], ['Mji', 'city'], ['Nchi', 'country'], ['Hali ya ajira', 'employmentStatus'],
  ['Kazi', 'occupation'], ['Mwajiri', 'employer'], ['Kipato cha mwezi', 'monthlyIncome'],
  ['Ndugu wa karibu', 'kinName'], ['Simu ya ndugu', 'kinPhone'], ['Uhusiano', 'kinRelationship'],
];

export default function AdminUsersPage() {
  const { lang } = useLanguage();
  const en = lang === 'en';
  const [users, setUsers] = useState<any[]>([]);
  const [filter, setFilter] = useState('PENDING');
  const [sel, setSel] = useState<any>(null);
  const [link, setLink] = useState('');
  const [msg, setMsg] = useState('');
  const [busy, setBusy] = useState(false);

  const load = async (status = filter) => {
    try {
      const res = await adminAPI.list(status || undefined);
      setUsers(res.data.data || []);
    } catch (e: any) {
      setMsg(e.response?.data?.message || 'Imeshindikana kupakia orodha');
    }
  };

  useEffect(() => { load(filter); /* eslint-disable-next-line */ }, [filter]);

  const open = async (id: number) => {
    setLink(''); setMsg('');
    const res = await adminAPI.get(id);
    setSel(res.data.data);
  };

  const approve = async () => {
    setBusy(true);
    try {
      const res = await adminAPI.approve(sel.id);
      setLink(res.data.data.setPasswordLink);
      setMsg('Amekubaliwa. Nakili link na umtumie.');
      await load();
      const d = await adminAPI.get(sel.id);
      setSel(d.data.data);
    } catch (e: any) {
      setMsg(e.response?.data?.message || 'Imeshindikana');
    } finally { setBusy(false); }
  };

  const reject = async () => {
    const reason = window.prompt('Sababu ya kukataa:');
    if (reason === null) return;
    setBusy(true);
    try {
      await adminAPI.reject(sel.id, reason);
      setSel(null);
      await load();
    } catch (e: any) {
      setMsg(e.response?.data?.message || 'Imeshindikana');
    } finally { setBusy(false); }
  };

  return (
    <div className="p-4 sm:p-6">
      <div className="flex flex-wrap items-center justify-between gap-3 mb-4">
        <div><h1 className="text-2xl font-bold text-gray-900 dark:text-white">{en?'Users':'Watumiaji'}</h1><p className="text-xs text-gray-500">{en?'Production-safe role and account controls':'Udhibiti salama wa role na akaunti'}</p></div>
        <LanguageSwitcher/><select value={filter} onChange={(e) => setFilter(e.target.value)} className="px-3 py-2 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-900 text-gray-900 dark:text-white">
          <option value="PENDING">Wanasubiri</option>
          <option value="APPROVED">Wamekubaliwa</option>
          <option value="REJECTED">Wamekataliwa</option>
          <option value="">Wote</option>
        </select>
      </div>

      {msg && !sel && <p className="mb-3 text-sm text-red-600">{msg}</p>}

      <div className="grid gap-3">
        {users.length === 0 && <p className="text-gray-500">Hakuna watumiaji.</p>}
        {users.map((u) => (

          <button key={u.id} onClick={() => open(u.id)} className="text-left bg-white dark:bg-gray-900 rounded-2xl p-4 border border-gray-200 dark:border-gray-800 hover:shadow-lg transition flex items-center justify-between gap-3">
            <div>
              <p className="font-semibold text-gray-900 dark:text-white">{u.fullName}</p>
              <p className="text-sm text-gray-600 dark:text-gray-400">{u.email} · {u.phone} · {u.role}</p>
            </div>
            <span className={`px-3 py-1 rounded-full text-xs font-semibold ${STATUS[u.status] || ''}`}>{u.status}</span>
          </button>
        ))}
      </div>

      {sel && (
        <div className="fixed inset-0 z-50 bg-black/60 flex items-center justify-center p-4" onClick={() => setSel(null)}>
          <div className="bg-white dark:bg-gray-900 rounded-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto p-6" onClick={(e) => e.stopPropagation()}>
            <div className="flex gap-4 items-start mb-4">
              {sel.photo ? (
                // eslint-disable-next-line @next/next/no-img-element
                <img src={sel.photo} alt="Picha" className="w-28 rounded-lg border border-gray-300 object-cover" style={{ aspectRatio: '35 / 45' }} />
              ) : (
                <div className="w-28 bg-gray-200 rounded-lg flex items-center justify-center text-3xl" style={{ aspectRatio: '35 / 45' }}>👤</div>
              )}
              <div className="flex-1">
                <h2 className="text-xl font-bold text-gray-900 dark:text-white">{sel.fullName}</h2>
                <p className="text-sm text-gray-600 dark:text-gray-400">{sel.role}</p>
                <span className={`inline-block mt-2 px-3 py-1 rounded-full text-xs font-semibold ${STATUS[sel.status] || ''}`}>{sel.status}</span>
                {sel.rejectionReason && <p className="mt-2 text-sm text-red-600">Sababu: {sel.rejectionReason}</p>}
              </div>
              <button onClick={() => setSel(null)} className="text-gray-500 text-xl">✕</button>
            </div>

            <div className="mt-5 grid gap-3 rounded-2xl bg-slate-50 p-4 dark:bg-slate-950">
              <div className="flex flex-wrap items-center justify-between gap-3">
                <div><b>{en?'Role & account controls':'Udhibiti wa role na akaunti'}</b><p className="text-xs text-gray-500">{en?'Admin accounts cannot be changed here.':'Akaunti za Admin haziwezi kubadilishwa hapa.'}</p></div>
                {sel.role !== 'ADMIN' && (
                  <div className="flex flex-wrap gap-2">
                    <select value={sel.role} onChange={async e=>{try{const r=await adminUserAPI.changeRole(sel.id,e.target.value);setSel(r.data.data);await load();setMsg(en?'Role updated.':'Role imebadilishwa.')}catch(e:any){setMsg(e.response?.data?.message||'Imeshindikana')}}} className="rounded-xl border px-3 py-2 text-sm dark:border-gray-700 dark:bg-gray-900">
                      <option value="BORROWER">BORROWER</option><option value="LENDER">LENDER</option><option value="BURSER">BURSER</option><option value="DIRECTOR">DIRECTOR / CEO</option>
                    </select>
                    <button onClick={async()=>{try{const r=await adminUserAPI.setActive(sel.id,!sel.active);setSel(r.data.data);await load();setMsg(en?'Account status updated.':'Hali ya akaunti imebadilishwa.')}catch(e:any){setMsg(e.response?.data?.message||'Imeshindikana')}}} className={`rounded-xl px-3 py-2 text-sm font-semibold ${sel.active?'bg-red-600 text-white':'bg-emerald-600 text-white'}`}>
                      {sel.active?(en?'Suspend':'Simamisha'):(en?'Activate':'Washa')}
                    </button>
                  </div>
                )}
              </div>
            </div>

            <dl className="grid sm:grid-cols-2 gap-x-6 gap-y-2 text-sm">
              {ROWS.map(([label, key]) => (
                <div key={key}>
                  <dt className="text-gray-500">{label}</dt>
                  <dd className="font-medium text-gray-900 dark:text-white break-words">{sel[key] ?? '-'}</dd>
                </div>
              ))}
            </dl>

            {msg && <p className="mt-4 text-sm text-green-700">{msg}</p>}
            {link && (
              <div className="mt-3 p-3 rounded-xl bg-blue-50 dark:bg-blue-950 border border-blue-200 dark:border-blue-800">
                <p className="text-xs text-blue-700 dark:text-blue-300 mb-1">Link ya kuweka password (saa 48):</p>
                <p className="text-xs break-all text-gray-900 dark:text-white">{link}</p>
                <button onClick={() => navigator.clipboard.writeText(link)} className="mt-2 px-3 py-1 text-xs font-semibold bg-blue-600 text-white rounded-lg">Nakili</button>
              </div>
            )}

            {sel.status !== 'APPROVED' || !sel.active ? (
              <div className="mt-6 flex gap-3">
                <button disabled={busy} onClick={approve} className="flex-1 py-2.5 rounded-xl bg-green-600 text-white font-semibold hover:bg-green-700 disabled:opacity-50">
                  {sel.status === 'APPROVED' ? 'Tengeneza link mpya' : '✅ Kubali'}
                </button>
                {sel.status !== 'REJECTED' && (
                  <button disabled={busy} onClick={reject} className="flex-1 py-2.5 rounded-xl bg-red-600 text-white font-semibold hover:bg-red-700 disabled:opacity-50">❌ Kataa</button>
                )}
              </div>
            ) : null}
          </div>
        </div>
      )}
    </div>
  );
}
