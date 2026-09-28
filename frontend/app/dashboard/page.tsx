'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';

export default function DashboardPage() {
  const router = useRouter();
  const [user, setUser] = useState<any>(null);

  useEffect(() => {
    const raw = localStorage.getItem('user');
    if (!localStorage.getItem('token') || !raw) { router.push('/login'); return; }
    const u = JSON.parse(raw);
    if (u.role === 'ADMIN') { router.push('/admin/dashboard'); return; }
    setUser(u);
  }, [router]);

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    document.cookie = 'token=; path=/; max-age=0';
    router.push('/login');
  };

  if (!user) return null;

  const stats = [
    { label: 'Mikopo Yote', value: '0', icon: '💰', color: 'from-blue-500 to-indigo-600' },
    { label: 'Imelipwa', value: '0', icon: '✅', color: 'from-green-500 to-emerald-600' },
    { label: 'Inasubiri', value: '0', icon: '⏳', color: 'from-yellow-500 to-orange-500' },
    { label: 'Imechelewa', value: '0', icon: '⚠️', color: 'from-red-500 to-pink-600' },
  ];

  const card = 'bg-white dark:bg-gray-900 rounded-2xl p-6 border border-gray-200 dark:border-gray-800 text-center';

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-950">
      <nav className="sticky top-0 z-50 bg-white dark:bg-gray-900 border-b border-gray-200 dark:border-gray-800 px-4 py-4">
        <div className="max-w-7xl mx-auto flex justify-between items-center">
          <Link href="/" className="flex items-center gap-2">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-xl">💰</div>
            <span className="font-bold text-lg bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">JmkLoanApp</span>
          </Link>
          <button onClick={logout} className="px-4 py-2 text-sm font-semibold text-red-600 hover:bg-red-50 dark:hover:bg-red-950 rounded-xl transition">Toka</button>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto p-6">
        <div className="bg-gradient-to-br from-blue-600 to-indigo-700 rounded-2xl p-8 text-white mb-8">
          <p className="text-sm opacity-80">Karibu,</p>
          <h1 className="text-3xl font-bold mb-2">{user.fullName}</h1>
          <p className="text-sm opacity-80">{user.role}</p>
        </div>

        <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
          {stats.map((s, i) => (
            <div key={i} className="bg-white dark:bg-gray-900 rounded-2xl p-6 border border-gray-200 dark:border-gray-800">
              <div className={`w-12 h-12 rounded-xl bg-gradient-to-br ${s.color} flex items-center justify-center text-2xl mb-4`}>{s.icon}</div>
              <p className="text-3xl font-bold text-gray-900 dark:text-white">{s.value}</p>
              <p className="text-sm text-gray-600 dark:text-gray-400">{s.label}</p>
            </div>
          ))}
        </div>

        <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {[['➕', 'Mkopo Mpya'], ['📋', 'Orodha ya Mikopo'], ['💳', 'Malipo']].map(([icon, label]) => (
            <div key={label} className={`${card} opacity-60`}>
              <div className="text-4xl mb-3">{icon}</div>
              <p className="font-semibold text-gray-900 dark:text-white">{label}</p>
              <p className="text-xs text-gray-500 mt-1">Inakuja</p>
            </div>
          ))}
          <Link href="/profile" className={`${card} hover:shadow-lg transition`}>
            <div className="text-4xl mb-3">👤</div>
            <p className="font-semibold text-gray-900 dark:text-white">Wasifu Wangu</p>
          </Link>
        </div>
      </div>
    </div>
  );
}
