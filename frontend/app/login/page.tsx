'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { ShieldCheck, ArrowLeft } from 'lucide-react';
import { authAPI } from '@/lib/api';
import { useAuth } from '@/store/auth';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import { useLanguage } from '@/lib/useLanguage';

export default function LoginPage() {
  const router = useRouter();
  const { setAuth } = useAuth();
  const { lang } = useLanguage();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const en = lang === 'en';
  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(''); setLoading(true);
    try {
      const res = await authAPI.login({ username, password });
      const userData = res.data.data;
      localStorage.setItem('token', userData.token);
      localStorage.setItem('user', JSON.stringify(userData));
      document.cookie = `token=${userData.token}; path=/; max-age=86400; SameSite=Lax`;
      setAuth(userData.token, userData);
      router.push(userData.role === 'ADMIN' ? '/admin/dashboard' : '/dashboard');
    } catch (err: any) {
      setError(err.response?.data?.message || (en ? 'Login failed. Check your credentials.' : 'Kuingia kumeshindikana. Hakikisha taarifa zako ni sahihi.'));
    } finally { setLoading(false); }
  };

  const cls='w-full rounded-xl border border-slate-300 bg-white px-4 py-3 text-slate-900 outline-none focus:ring-2 focus:ring-blue-500 dark:border-slate-700 dark:bg-slate-950 dark:text-white';
  return <main className="min-h-screen bg-slate-950 p-4">
    <div className="mx-auto flex min-h-[calc(100vh-2rem)] max-w-6xl items-center justify-center">
      <div className="grid w-full overflow-hidden rounded-[2rem] border border-white/10 bg-white shadow-2xl dark:bg-slate-900 lg:grid-cols-2">
        <div className="hidden bg-gradient-to-br from-blue-700 via-indigo-800 to-slate-950 p-10 text-white lg:flex lg:flex-col lg:justify-between">
          <Link href="/" className="flex items-center gap-2 font-black"><span className="text-2xl">💰</span> JmkLoanApp</Link>
          <div><ShieldCheck className="h-12 w-12 text-cyan-300" /><h1 className="mt-6 text-4xl font-black">{en?'Secure access to your loan workspace.':'Fikia mfumo wako wa mikopo kwa usalama.'}</h1><p className="mt-4 text-blue-100">{en?'Manage loans, payments and guarantors from one place.':'Simamia mikopo, malipo na wadhamini sehemu moja.'}</p></div>
          <p className="text-xs text-blue-200">JWT • Role-based access • Protected API</p>
        </div>
        <div className="p-6 sm:p-10">
          <div className="flex items-center justify-between"><Link href="/" className="text-sm font-semibold text-blue-600"><ArrowLeft className="mr-1 inline h-4 w-4"/>{en?'Home':'Nyumbani'}</Link><LanguageSwitcher /></div>
          <div className="mx-auto mt-8 max-w-md">
            <div className="mb-7"><div className="text-3xl">🔐</div><h2 className="mt-3 text-3xl font-black">{en?'Welcome back':'Karibu tena'}</h2><p className="mt-2 text-sm text-slate-500">{en?'Sign in to continue to your dashboard.':'Ingia kuendelea kwenye dashboard yako.'}</p></div>
            {error && <div className="mb-5 rounded-xl border border-red-200 bg-red-50 p-3 text-sm text-red-700 dark:border-red-900 dark:bg-red-950/40 dark:text-red-300">⚠️ {error}</div>}
            <form onSubmit={handleLogin} className="space-y-5">
              <label className="block text-sm font-bold">{en?'Email or phone':'Barua pepe au simu'}<input value={username} onChange={e=>setUsername(e.target.value)} required className={`${cls} mt-1.5`} autoComplete="username"/></label>
              <label className="block text-sm font-bold">Password<input type="password" value={password} onChange={e=>setPassword(e.target.value)} required className={`${cls} mt-1.5`} autoComplete="current-password"/></label>
              <button disabled={loading} className="w-full rounded-xl bg-blue-600 py-3.5 font-black text-white shadow-lg hover:bg-blue-700 disabled:opacity-50">{loading?(en?'Signing in...':'Inaingia...'):(en?'Sign in':'Ingia')}</button>
            </form>
            <p className="mt-7 text-center text-sm text-slate-500">{en?'No account?':'Huna akaunti?'} <Link href="/register" className="font-bold text-blue-600 hover:underline">{en?'Create one':'Jisajili'}</Link></p>
          </div>
        </div>
      </div>
    </div>
  </main>;
}
