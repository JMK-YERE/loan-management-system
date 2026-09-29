'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { Activity, ArrowRight, CreditCard, FileText, RefreshCw, Users, WalletCards } from 'lucide-react';
import { adminAPI } from '@/lib/api';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import { useLanguage } from '@/lib/useLanguage';

const money=(v:any)=>new Intl.NumberFormat('sw-TZ',{style:'currency',currency:'TZS',maximumFractionDigits:0}).format(Number(v||0));

export default function AdminDashboard(){
  const {lang}=useLanguage(); const en=lang==='en';
  const [stats,setStats]=useState<any>(null); const [error,setError]=useState('');
  const load=async()=>{try{const r=await adminAPI.stats();setStats(r.data.data);}catch(e:any){setError(e.response?.data?.message||(en?'Unable to load statistics.':'Imeshindikana kupakia takwimu.'));}};
  useEffect(()=>{load();},[]);
  const cards=[
    [en?'Users':'Watumiaji',stats?.totalUsers||0,Users],
    [en?'Pending users':'Watumiaji wanaosubiri',stats?.pendingUsers||0,Activity],
    [en?'Total loans':'Mikopo yote',stats?.totalLoans||0,FileText],
    [en?'Portfolio':'Thamani ya mikopo',money(stats?.portfolioAmount),WalletCards],
    [en?'Successful repayments':'Marejesho yaliyothibitishwa',money(stats?.successfulRepayments),CreditCard],
    [en?'Defaulted loans':'Mikopo iliyochelewa',stats?.defaultedLoans||0,Activity],
  ];
  return <div className="p-4 sm:p-6 lg:p-8">
    <div className="mb-8 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
      <div><p className="text-sm font-bold text-blue-600">JmkLoanApp</p><h1 className="text-3xl font-black text-slate-900 dark:text-white">{en?'Admin dashboard':'Dashboard ya Admin'}</h1><p className="mt-1 text-slate-500">{en?'Live overview of users, loans and repayments.':'Muhtasari wa moja kwa moja wa watumiaji, mikopo na marejesho.'}</p></div>
      <div className="flex gap-2"><LanguageSwitcher/><button onClick={load} className="rounded-xl border p-2.5 dark:border-slate-700"><RefreshCw className="h-4 w-4"/></button></div>
    </div>
    {error&&<div className="mb-5 rounded-xl bg-red-50 p-3 text-sm text-red-700">{error}</div>}
    <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
      {cards.map(([label,value,Icon]:any)=><div key={label} className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm dark:border-slate-800 dark:bg-slate-900"><div className="flex h-11 w-11 items-center justify-center rounded-xl bg-blue-50 text-blue-600 dark:bg-blue-950"><Icon className="h-5 w-5"/></div><div className="mt-4 text-2xl font-black text-slate-900 dark:text-white">{value}</div><div className="text-sm text-slate-500">{label}</div></div>)}
    </div>
    <div className="mt-6 grid gap-5 lg:grid-cols-3">
      <div className="rounded-3xl bg-gradient-to-br from-blue-700 to-indigo-900 p-7 text-white lg:col-span-2"><h2 className="text-2xl font-black">{en?'Operations overview':'Muhtasari wa shughuli'}</h2><div className="mt-6 grid grid-cols-2 gap-4 sm:grid-cols-4">{[[en?'Pending loans':'Zinasubiri',stats?.pendingLoans||0],[en?'Approved/disbursed':'Imeidhinishwa',stats?.approvedLoans||0],[en?'Paid':'Imelipwa',stats?.paidLoans||0],[en?'Payments':'Malipo',stats?.totalPayments||0]].map(([x,v])=><div key={x} className="rounded-2xl bg-white/10 p-4"><div className="text-2xl font-black">{v}</div><div className="text-xs text-blue-100">{x}</div></div>)}</div></div>
      <div className="rounded-3xl border border-slate-200 bg-white p-6 dark:border-slate-800 dark:bg-slate-900"><h2 className="font-black">{en?'Quick actions':'Vitendo vya haraka'}</h2><div className="mt-4 space-y-2"><Link href="/admin/users" className="flex items-center justify-between rounded-xl bg-slate-50 p-3 font-semibold dark:bg-slate-950">{en?'Review users':'Kagua watumiaji'}<ArrowRight className="h-4 w-4"/></Link><Link href="/admin/announcements" className="flex items-center justify-between rounded-xl bg-slate-50 p-3 font-semibold dark:bg-slate-950">{en?'Announcements':'Matangazo'}<ArrowRight className="h-4 w-4"/></Link></div></div>
    </div>
  </div>;
}
