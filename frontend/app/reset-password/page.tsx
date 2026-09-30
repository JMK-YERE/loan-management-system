'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { authAPI } from '@/lib/api';
import { useLanguage } from '@/lib/useLanguage';

export default function ResetPasswordPage() {
  const router = useRouter();
  const { lang } = useLanguage();
  const en = lang === 'en';
  const [token, setToken] = useState('');
  const [pw, setPw] = useState('');
  const [pw2, setPw2] = useState('');
  const [error, setError] = useState('');
  const [ok, setOk] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setToken(new URLSearchParams(window.location.search).get('token') || '');
  }, []);

  const submit = async (e: React.FormEvent) => {
    e.preventDefault(); setError('');
    if (!token) return setError(en ? 'Invalid or missing reset link.' : 'Link ya reset si sahihi.');
    if (pw.length < 8) return setError(en ? 'Password must be at least 8 characters.' : 'Password iwe angalau herufi 8.');
    if (pw !== pw2) return setError(en ? 'Passwords do not match.' : 'Password hazifanani.');
    setLoading(true);
    try {
      await authAPI.resetPassword({ token, password: pw });
      setOk(true);
      setTimeout(() => router.push('/login'), 2500);
    } catch (err: any) {
      setError(err.response?.data?.message || (en ? 'Reset link is invalid or expired.' : 'Link ya reset si sahihi au imeisha muda.'));
    } finally { setLoading(false); }
  };

  const cls='w-full rounded-xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none focus:ring-2 focus:ring-blue-500 dark:border-slate-700 dark:bg-slate-950 dark:text-white';

  return <main className="min-h-screen bg-slate-950 p-4 flex items-center justify-center">
    <div className="w-full max-w-md rounded-[2rem] bg-white p-7 shadow-2xl dark:bg-slate-900 sm:p-10">
      <h1 className="text-3xl font-black dark:text-white">🔑 {en?'Reset password':'Badilisha password'}</h1>
      <p className="mt-2 text-sm text-slate-500">{en?'Create a new secure password.':'Weka password mpya salama.'}</p>
      {error && <div className="mt-5 rounded-xl border border-red-200 bg-red-50 p-3 text-sm text-red-700">⚠️ {error}</div>}
      {ok && <div className="mt-5 rounded-xl border border-green-200 bg-green-50 p-3 text-sm text-green-700">✅ {en?'Password changed. Redirecting to login...':'Password imebadilishwa. Unaelekezwa kwenye login...'}</div>}
      <form onSubmit={submit} className="mt-6 space-y-4">
        <input type="password" required value={pw} onChange={e=>setPw(e.target.value)} placeholder={en?'New password':'Password mpya'} className={cls} autoComplete="new-password" />
        <input type="password" required value={pw2} onChange={e=>setPw2(e.target.value)} placeholder={en?'Confirm password':'Rudia password'} className={cls} autoComplete="new-password" />
        <button disabled={loading || !token} className="w-full rounded-xl bg-blue-600 py-3.5 font-black text-white disabled:opacity-50">{loading?(en?'Saving...':'Inahifadhi...'):(en?'Change password':'Badilisha password')}</button>
      </form>
      <p className="mt-6 text-center text-sm"><Link href="/login" className="font-bold text-blue-600">{en?'Back to login':'Rudi kwenye login'}</Link></p>
    </div>
  </main>;
}
