'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { useAuth } from '@/store/auth';

export default function LoginPage() {
  const router = useRouter();
  const { setAuth } = useAuth();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    // ADMIN LOGIN - HARDCODED
    if (username === 'admin' && password === 'admin@123') {
      const adminData = {
        token: 'admin-token-' + Date.now(),
        userId: 1,
        fullName: 'Administrator',
        email: 'admin@jmkloanapp.co.tz',
        role: 'ADMIN',
      };
      localStorage.setItem('token', adminData.token);
      localStorage.setItem('user', JSON.stringify(adminData));
      document.cookie = `token=${adminData.token}; path=/; max-age=86400`;
      setAuth(adminData.token, adminData);
      router.push('/admin/dashboard');
      return;
    }

    // USER LOGIN - KUPITIA BACKEND
    try {
      const res = await fetch('https://jmkloanapp-backend.onrender.com/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });
      const data = await res.json();
      if (data.success) {
        const userData = data.data;
        localStorage.setItem('token', userData.token);
        localStorage.setItem('user', JSON.stringify(userData));
        document.cookie = `token=${userData.token}; path=/; max-age=86400`;
        setAuth(userData.token, userData);
        router.push('/dashboard');
      } else {
        setError(data.message || 'Kuingia kumeshindikana');
      }
    } catch (err: any) {
      setError('Backend haipatikani. Jaribu tena baada ya sekunde 30.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-indigo-100 dark:from-gray-950 dark:via-gray-900 dark:to-indigo-950 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <Link href="/" className="flex items-center justify-center gap-2 mb-8">
          <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-2xl shadow-lg">💰</div>
          <span className="text-2xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">JmkLoanApp</span>
        </Link>
        <div className="bg-white dark:bg-gray-900 rounded-2xl p-8 shadow-xl border border-gray-200 dark:border-gray-800">
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-2">Karibu Tena</h1>
          <p className="text-gray-600 dark:text-gray-400 mb-6">Ingia kwenye akaunti yako</p>
          {error && <div className="mb-4 p-3 rounded-xl bg-red-50 dark:bg-red-950 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-300 text-sm">⚠️ {error}</div>}
          <form onSubmit={handleLogin} className="space-y-4">
            <div>
              <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Username au Email</label>
              <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} required className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500" />
            </div>
            <div>
              <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">Password</label>
              <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required className="w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500" />
            </div>
            <button type="submit" disabled={loading} className="w-full px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 text-white font-bold rounded-xl hover:from-blue-700 hover:to-indigo-700 transition shadow-lg disabled:opacity-50">
              {loading ? 'Inaingia...' : 'Ingia'}
            </button>
          </form>
          <p className="mt-6 text-center text-sm text-gray-600 dark:text-gray-400">
            Hauna akaunti? <Link href="/register" className="text-blue-600 font-semibold hover:underline">Jisajili</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
