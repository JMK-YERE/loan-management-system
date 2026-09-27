'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { LayoutDashboard, Megaphone, Users, Settings, LogOut, ArrowLeft } from 'lucide-react';
import { useAuth } from '@/store/auth';

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const { user, logout } = useAuth();

  const links = [
    { href: '/admin/dashboard', label: 'Dashboard', icon: LayoutDashboard },
    { href: '/admin/announcements', label: 'Matangazo', icon: Megaphone },
    { href: '/admin/users', label: 'Watumiaji', icon: Users },
    { href: '/admin/settings', label: 'Mipangilio', icon: Settings },
  ];

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-950 flex">
      <aside className="w-64 bg-white dark:bg-gray-900 border-r border-gray-200 dark:border-gray-800 flex flex-col">
        <div className="p-6 border-b border-gray-200 dark:border-gray-800">
          <Link href="/" className="flex items-center gap-2">
            <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-xl shadow-lg">💰</div>
            <span className="text-lg font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">Admin</span>
          </Link>
        </div>
        <nav className="flex-1 p-4 space-y-1">
          {links.map((l) => {
            const Icon = l.icon;
            const active = pathname === l.href;
            return (
              <Link key={l.href} href={l.href} className={`flex items-center gap-3 px-4 py-3 rounded-xl transition-all ${active ? 'bg-gradient-to-r from-blue-600 to-indigo-600 text-white shadow-lg' : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800'}`}>
                <Icon className="w-5 h-5" />
                <span className="font-semibold">{l.label}</span>
              </Link>
            );
          })}
        </nav>
        <div className="p-4 border-t border-gray-200 dark:border-gray-800">
          <div className="mb-3 px-4">
            <p className="text-xs text-gray-500">Umeingia kama</p>
            <p className="font-bold text-sm text-gray-900 dark:text-white truncate">{user?.fullName || 'Admin'}</p>
          </div>
          <Link href="/" className="flex items-center gap-3 px-4 py-2 rounded-xl text-gray-600 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-800 transition mb-1">
            <ArrowLeft className="w-4 h-4" />
            <span className="text-sm font-semibold">Rudi Tovuti</span>
          </Link>
          <button onClick={logout} className="w-full flex items-center gap-3 px-4 py-2 rounded-xl text-red-600 hover:bg-red-50 dark:hover:bg-red-950 transition">
            <LogOut className="w-4 h-4" />
            <span className="text-sm font-semibold">Toka</span>
          </button>
        </div>
      </aside>
      <main className="flex-1 overflow-auto">{children}</main>
    </div>
  );
}
