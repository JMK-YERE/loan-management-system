'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { authAPI } from '@/lib/api';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import { useLanguage } from '@/lib/useLanguage';

export default function SetPasswordPage() {
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
    e.preventDefault();
    setError('');
    if (pw.length < 8) return setError(en ? 'Password must be at least 8 characters' : 'Password iwe angalau herufi 8');
    if (pw !== pw2) return setError(en ? 'Passwords do not match' : 'Password hazifanani');
    setLoading(true);
    try {
      await authAPI.setPassword({ token, password: pw });
      setOk(true);
      setTimeout(() => router.push('/login'), 3000);
    } catch (err: any) {
      setError(err.response?.data?.message || (en ? 'Something went wrong. Try again.' : 'Imeshindikana. Jaribu tena.'));
    } finally { setLoading(false); }
  };

  const cls = 'w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500';

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-indigo-100 dark:from-gray-950 dark:via-gray-900 dark:to-indigo-950 flex items-center justify-center p-4">
      <div className="w-full max-w-md bg-white dark:bg-gray-900 rounded-2xl p-8 shadow-xl border border-gray-200 dark:border-gray-800">
        <div className="mb-4 flex justify-end"><LanguageSwitcher /></div>
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-2">{en ? 'Set Password' : 'Weka Password'}</h1>
        <p className="text-gray-600 dark:text-gray-400 mb-6">{en ? 'Your account has been approved. Create a new password.' : 'Akaunti yako imekubaliwa. Weka password mpya.'}</p>
        {!token && <p className="text-red-600 text-sm mb-3">{en ? 'The link has no token. Request a new link from an administrator.' : 'Link haina token. Omba link mpya kwa admin.'}</p>}
        {error && <div className="mb-4 p-3 rounded-xl bg-red-50 border border-red-200 text-red-700 text-sm">⚠️ {error}</div>}
        {ok && <div className="mb-4 p-3 rounded-xl bg-green-50 border border-green-200 text-green-700 text-sm">✅ {en ? 'Password saved. Redirecting to login...' : 'Password imewekwa. Unaelekezwa kwenye login...'}</div>}
        <form onSubmit={submit} className="space-y-4">
          <input type="password" required placeholder={en ? 'New password' : 'Password mpya'} value={pw} onChange={(e) => setPw(e.target.value)} className={cls} />
          <input type="password" required placeholder={en ? 'Confirm password' : 'Rudia password'} value={pw2} onChange={(e) => setPw2(e.target.value)} className={cls} />
          <button disabled={loading || !token} className="w-full px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 text-white font-bold rounded-xl disabled:opacity-50">
            {loading ? (en ? 'Saving...' : 'Inahifadhi...') : (en ? 'Save Password' : 'Hifadhi Password')}
          </button>
        </form>
      </div>
    </div>
  );
}
