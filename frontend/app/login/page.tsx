'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { ShieldCheck, ArrowLeft } from 'lucide-react';
import Script from 'next/script';
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
  const googleClientId = process.env.NEXT_PUBLIC_GOOGLE_CLIENT_ID || '';
  const appleClientId = process.env.NEXT_PUBLIC_APPLE_CLIENT_ID || '';
  const handleGoogle = async (credential: string) => { setError(''); setLoading(true); try { const res=await authAPI.google(credential); const userData=res.data.data; localStorage.setItem('token',userData.token); localStorage.setItem('user',JSON.stringify(userData)); document.cookie=`token=${userData.token}; path=/; max-age=86400; SameSite=Lax`; setAuth(userData.token,userData); router.push(userData.role==='ADMIN'?'/admin/dashboard':'/dashboard'); } catch(err:any) { setError(err.response?.data?.message || (en?'Google sign-in failed.':'Google sign-in imeshindikana.')); } finally { setLoading(false); } };

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
            {googleClientId && <Script src="https://accounts.google.com/gsi/client" strategy="afterInteractive" onLoad={() => { const g=(window as any).google; if(g){ g.accounts.id.initialize({client_id:googleClientId,callback:(r:any)=>handleGoogle(r.credential)}); g.accounts.id.renderButton(document.getElementById('google-signin')!,{theme:'outline',size:'large',width:380,text:'continue_with'}); } }} />}
            <div className="mb-5 space-y-3">
              {googleClientId ? <div id="google-signin" className="flex justify-center" /> : <button type="button" onClick={()=>setError(en?'Google Sign-In is not configured on this deployment yet.':'Google Sign-In bado haijawekewa Google Client ID kwenye deployment hii.')} className="w-full rounded-xl border border-slate-300 bg-white py-3 font-black text-slate-700 shadow-sm hover:bg-slate-50 dark:border-slate-700 dark:bg-slate-950 dark:text-white">Continue with Google</button>}
              {appleClientId ? <button type="button" onClick={()=>setError(en?'Apple Sign-In configuration is present, but the Apple authentication endpoint still needs to be enabled.':'Apple Sign-In imewekewa Client ID, lakini endpoint ya uthibitishaji wa Apple bado inahitaji kuwashwa.')} className="w-full rounded-xl border border-slate-900 bg-slate-900 py-3 font-black text-white shadow-sm hover:bg-black dark:border-slate-600 dark:bg-black"> Continue with Apple</button> : <button type="button" onClick={()=>setError(en?'Apple Sign-In is not configured on this deployment yet.':'Apple/iPhone Sign-In bado haijawekewa Apple Client ID kwenye deployment hii.')} className="w-full rounded-xl border border-slate-300 bg-white py-3 font-black text-slate-800 shadow-sm hover:bg-slate-50 dark:border-slate-700 dark:bg-slate-950 dark:text-white"> Continue with Apple</button>}
            </div>
            <form onSubmit={handleLogin} className="space-y-5">
              <label className="block text-sm font-bold">{en?'Email or phone':'Barua pepe au simu'}<input value={username} onChange={e=>setUsername(e.target.value)} required className={`${cls} mt-1.5`} autoComplete="username"/></label>
              <label className="block text-sm font-bold">Password<input type="password" value={password} onChange={e=>setPassword(e.target.value)} required className={`${cls} mt-1.5`} autoComplete="current-password"/></label>
              <div className="text-right -mt-2"><Link href="/forgot-password" className="text-sm font-bold text-blue-600 hover:underline">{en?'Forgot password?':'Umesahau password?'}</Link></div>
              <button disabled={loading} className="w-full rounded-xl bg-blue-600 py-3.5 font-black text-white shadow-lg hover:bg-blue-700 disabled:opacity-50">{loading?(en?'Signing in...':'Inaingia...'):(en?'Sign in':'Ingia')}</button>
            </form>
            <p className="mt-7 text-center text-sm text-slate-500">{en?'No account?':'Huna akaunti?'} <Link href="/register" className="font-bold text-blue-600 hover:underline">{en?'Create one':'Jisajili'}</Link></p>
          </div>
        </div>
      </div>
    </div>
  </main>;
}
