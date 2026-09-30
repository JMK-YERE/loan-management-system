'use client';

import { useState } from 'react';
import Link from 'next/link';
import { authAPI } from '@/lib/api';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import { useLanguage } from '@/lib/useLanguage';

export default function ForgotPasswordPage() {
  const { lang } = useLanguage();
  const en = lang === 'en';
  const [email, setEmail] = useState('');
  const [loading, setLoading] = useState(false);
  const [ok, setOk] = useState(false);
  const [error, setError] = useState('');

  const submit = async (e: React.FormEvent) => {
    e.preventDefault(); setError(''); setOk(false); setLoading(true);
    try {
      await authAPI.forgotPassword({ email });
      setOk(true);
    } catch (err: any) {
      setError(err.response?.data?.message || (en ? 'Request failed. Please try again.' : 'Ombi limeshindwa. Jaribu tena.'));
    } finally { setLoading(false); }
  };

  const cls='w-full rounded-xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none focus:ring-2 focus:ring-blue-500 dark:border-slate-700 dark:bg-slate-950 dark:text-white';

  return <main className="min-h-screen bg-slate-950 p-4 flex items-center justify-center">
    <div className="w-full max-w-md rounded-[2rem] bg-white p-7 shadow-2xl dark:bg-slate-900 sm:p-10">
      <div className="flex justify-end"><LanguageSwitcher /></div>
      <div className="mt-4 text-3xl">🔐</div>
      <h1 className="mt-3 text-3xl font-black dark:text-white">{en?'Forgot password?':'Umesahau password?'}</h1>
      <p className="mt-2 text-sm text-slate-500">{en?'Enter your account email and we will send a secure reset link.':'Weka email ya akaunti yako na tutakutumia link salama ya kubadilisha password.'}</p>
      {error && <div className="mt-5 rounded-xl border border-red-200 bg-red-50 p-3 text-sm text-red-700">⚠️ {error}</div>}
      {ok && <div className="mt-5 rounded-xl border border-green-200 bg-green-50 p-3 text-sm text-green-700">✅ {en?'If the account is eligible, a reset email has been sent. Check your inbox and spam folder.':'Kama akaunti inaruhusiwa, email ya reset imetumwa. Angalia Inbox na Spam.'}</div>}
      <form onSubmit={submit} className="mt-6 space-y-4">
        <input type="email" required value={email} onChange={e=>setEmail(e.target.value)} placeholder={en?'Email address':'Barua pepe'} className={cls} autoComplete="email" />
        <button disabled={loading} className="w-full rounded-xl bg-blue-600 py-3.5 font-black text-white disabled:opacity-50">{loading?(en?'Sending...':'Inatuma...'):(en?'Send reset link':'Tuma link ya reset')}</button>
      </form>
      <p className="mt-6 text-center text-sm text-slate-500"><Link href="/login" className="font-bold text-blue-600">{en?'Back to login':'Rudi kwenye login'}</Link></p>
    </div>
  </main>;
}
