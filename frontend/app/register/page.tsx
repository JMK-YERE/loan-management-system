'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { authAPI } from '@/lib/api';

export default function RegisterPage() {
  const router = useRouter();
  const [form, setForm] = useState({ fullName: '', email: '', phone: '', nidaNumber: '', role: 'LENDER' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);
    try {
      const res = await authAPI.register(form);
      setSuccess(res.data.message || 'Usajili umefanikiwa! Admin atakagua.');
      setTimeout(() => router.push('/login'), 4000);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Usajili umeshindikana');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-indigo-100 dark:from-gray-950 dark:via-gray-900 dark:to-indigo-950 flex items-center justify-center p-4 py-12">
      <div className="w-full max-w-md">
        <Link href="/" className="flex items-center justify-center gap-2 mb-8">
          <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-2xl shadow-lg">💰</div>
          <span className="text-2xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">JmkLoanApp</span>
        </Link>
        <div className="bg-white dark:bg-gray-900 rounded-2xl p-8 shadow-xl border border-gray-200 dark:border-gray-800">
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-2">Jisajili</h1>
          <p className="text-gray-600 dark:text-gray-400 mb-6">Unda akaunti yako</p>
          {error && <div className="mb-4 p-3 rounded-xl bg-red-50 dark:bg-red-950 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-300 text-sm">⚠️ {error}</div>}
          {success && <div className="mb-4 p-3 rounded-xl bg-green-50 dark:bg-green-950 border border-green-200 dark:border-green-800 text-green-700 dark:text-green-300 text-sm">✅ {success}</div>}
          <form onSubmit={handleRegister} className="space-y-4">
            <div>
              <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Jina Kamili</label>
              <input type="text" required value={form.fullName} onChange={(e) => setForm({...form, fullName: e.target.value})} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500" placeholder="Joseph Mkopeshaji" />
            </div>
            <div>
              <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Barua Pepe</label>
              <input type="email" required value={form.email} onChange={(e) => setForm({...form, email: e.target.value})} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500" placeholder="joseph@example.com" />
            </div>
            <div>
              <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Namba ya Simu</label>
              <input type="tel" required value={form.phone} onChange={(e) => setForm({...form, phone: e.target.value})} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500" placeholder="+255712345678" />
            </div>
            <div>
              <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Namba ya NIDA (tarakimu 20)</label>
              <input type="text" required maxLength={20} minLength={20} value={form.nidaNumber} onChange={(e) => setForm({...form, nidaNumber: e.target.value})} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500" placeholder="12345678901234567890" />
            </div>
            <div>
              <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Aina ya Akaunti</label>
              <select value={form.role} onChange={(e) => setForm({...form, role: e.target.value})} className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500">
                <option value="LENDER">Mkopeshaji</option>
                <option value="BORROWER">Mkopaji</option>
                <option value="GUARANTOR">Mdhamini</option>
              </select>
            </div>
            <button type="submit" disabled={loading} className="w-full px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 text-white font-bold rounded-xl hover:from-blue-700 hover:to-indigo-700 transition shadow-lg disabled:opacity-50">
              {loading ? 'Inatuma...' : 'Jisajili'}
            </button>
          </form>
          <p className="mt-6 text-center text-sm text-gray-600 dark:text-gray-400">
            Una akaunti? <Link href="/login" className="text-blue-600 font-semibold hover:underline">Ingia</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
