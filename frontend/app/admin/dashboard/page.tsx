'use client';

import { Megaphone, Users, TrendingUp, Activity } from 'lucide-react';

export default function AdminDashboard() {
  const stats = [
    { label: 'Matangazo', value: '0', icon: Megaphone, color: 'from-blue-500 to-indigo-600' },
    { label: 'Watumiaji', value: '0', icon: Users, color: 'from-green-500 to-emerald-600' },
    { label: 'Mikopo', value: '0', icon: TrendingUp, color: 'from-yellow-500 to-orange-500' },
    { label: 'Shughuli Leo', value: '0', icon: Activity, color: 'from-purple-500 to-pink-600' },
  ];

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-2">Dashboard</h1>
        <p className="text-gray-600 dark:text-gray-400">Karibu kwenye Admin Panel ya JmkLoanApp</p>
      </div>

      <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {stats.map((s, i) => {
          const Icon = s.icon;
          return (
            <div key={i} className="bg-white dark:bg-gray-900 rounded-2xl p-6 border border-gray-200 dark:border-gray-800">
              <div className={`w-12 h-12 rounded-xl bg-gradient-to-br ${s.color} flex items-center justify-center mb-4 shadow-lg`}>
                <Icon className="w-6 h-6 text-white" />
              </div>
              <p className="text-3xl font-bold text-gray-900 dark:text-white">{s.value}</p>
              <p className="text-sm text-gray-600 dark:text-gray-400">{s.label}</p>
            </div>
          );
        })}
      </div>

      <div className="bg-white dark:bg-gray-900 rounded-2xl p-8 border border-gray-200 dark:border-gray-800">
        <h2 className="text-xl font-bold text-gray-900 dark:text-white mb-4">Karibu!</h2>
        <p className="text-gray-600 dark:text-gray-400 mb-4">
          Hapa unaweza kusimamia matangazo, watumiaji, na mipangilio ya mfumo.
        </p>
        <a href="/admin/announcements" className="inline-flex items-center gap-2 px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 text-white font-bold rounded-xl hover:from-blue-700 hover:to-indigo-700 transition shadow-lg">
          Anza Kusimamia Matangazo
        </a>
      </div>
    </div>
  );
}
