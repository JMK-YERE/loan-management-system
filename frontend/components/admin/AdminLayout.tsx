'use client';

import { useState } from 'react';
import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { LayoutDashboard, Megaphone, Users, Settings, LogOut, ArrowLeft, Menu, X, BarChart3, WalletCards, ShieldCheck, Calculator, ClipboardList } from 'lucide-react';
import { useAuth } from '@/store/auth';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import { useLanguage } from '@/lib/useLanguage';

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const { user, logout } = useAuth();
  const { lang } = useLanguage();
  const en = lang === 'en';
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const links = [
    { href: '/admin/dashboard', label: en ? 'Dashboard' : 'Dashboard', icon: LayoutDashboard },
    { href: '/admin/announcements', label: en ? 'Announcements' : 'Matangazo', icon: Megaphone },
    { href: '/admin/users', label: en ? 'Users' : 'Watumiaji', icon: Users },
    { href: '/admin/reports', label: en ? 'Reports' : 'Ripoti', icon: BarChart3 },
    { href: '/admin/loan-products', label: en ? 'Loan Products' : 'Bidhaa za Mikopo', icon: WalletCards },
    { href: '/admin/credit-assessments', label: en ? 'Credit Assessment' : 'Tathmini ya Mikopo', icon: Calculator },
    { href: '/admin/operations', label: en ? 'Operations & Compliance' : 'Operesheni & Compliance', icon: ClipboardList },
    { href: '/admin/features', label: en ? 'All Features' : 'Vipengele Vyote', icon: ShieldCheck },
    { href: '/admin/organization', label: en ? 'Organizations & Branches' : 'Taasisi & Matawi', icon: Users },
    { href: '/admin/settings', label: en ? 'Settings' : 'Mipangilio', icon: Settings },
  ];

  const SidebarContent = () => (
    <>
      <div className="p-6 border-b border-gray-200 dark:border-gray-800 flex items-center justify-between">
        <Link href="/" className="flex items-center gap-2">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-xl shadow-lg">💰</div>
          <span className="text-lg font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">Admin</span>
        </Link>
        <button onClick={() => setSidebarOpen(false)} className="lg:hidden p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800">
          <X className="w-5 h-5 text-gray-600 dark:text-gray-300" />
        </button>
      </div>
      <nav className="flex-1 p-4 space-y-1">
        {links.map((l) => {
          const Icon = l.icon;
          const active = pathname === l.href;
          return (
            <Link key={l.href} href={l.href} onClick={() => setSidebarOpen(false)} className={`flex items-center gap-3 px-4 py-3 rounded-xl transition-all ${active ? 'bg-gradient-to-r from-blue-600 to-indigo-600 text-white shadow-lg' : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800'}`}>
              <Icon className="w-5 h-5" />
              <span className="font-semibold">{l.label}</span>
            </Link>
          );
        })}
      </nav>
      <div className="p-4 border-t border-gray-200 dark:border-gray-800">
        <div className="mb-3 px-4">
          <p className="text-xs text-gray-500">{en ? 'Signed in as' : 'Umeingia kama'}</p>
          <p className="font-bold text-sm text-gray-900 dark:text-white truncate">{user?.fullName || 'Admin'}</p>
        </div>
        <Link href="/" className="flex items-center gap-3 px-4 py-2 rounded-xl text-gray-600 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-800 transition mb-1">
          <ArrowLeft className="w-4 h-4" />
          <span className="text-sm font-semibold">{en ? 'Back to site' : 'Rudi Tovuti'}</span>
        </Link>
        <button onClick={logout} className="w-full flex items-center gap-3 px-4 py-2 rounded-xl text-red-600 hover:bg-red-50 dark:hover:bg-red-950 transition">
          <LogOut className="w-4 h-4" />
          <span className="text-sm font-semibold">{en ? 'Logout' : 'Toka'}</span>
        </button>
      </div>
    </>
  );

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-950 flex">
      <div className="lg:hidden fixed top-0 left-0 right-0 z-40 bg-white dark:bg-gray-900 border-b border-gray-200 dark:border-gray-800 px-4 py-3 flex items-center justify-between">
        <button onClick={() => setSidebarOpen(true)} className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-800">
          <Menu className="w-6 h-6 text-gray-900 dark:text-white" />
        </button>
        <Link href="/" className="flex items-center gap-2">
          <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-sm shadow-lg">💰</div>
          <span className="font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">Admin</span>
        </Link>
        <div className="w-10" />
      </div>

      {sidebarOpen && (
        <div className="lg:hidden fixed inset-0 bg-black/50 z-40" onClick={() => setSidebarOpen(false)} />
      )}

      <aside className={`fixed lg:static inset-y-0 left-0 z-50 w-64 bg-white dark:bg-gray-900 border-r border-gray-200 dark:border-gray-800 flex flex-col transform transition-transform duration-300 ${sidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}`}>
        <SidebarContent />
      </aside>

      <main className="flex-1 overflow-auto lg:pt-0 pt-16">
        {children}
      </main>
    </div>
  );
}
