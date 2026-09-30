'use client';

import { useEffect, useMemo, useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { CheckCircle2, Clock3, CreditCard, FileText, LogOut, RefreshCw, ShieldCheck, WalletCards } from 'lucide-react';
import { loanAPI, paymentAPI } from '@/lib/api';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import { useLanguage } from '@/lib/useLanguage';

const money=(v:any)=>new Intl.NumberFormat('sw-TZ',{style:'currency',currency:'TZS',maximumFractionDigits:0}).format(Number(v||0));
const unwrap=(r:any)=>r?.data?.data??r?.data??[];
const labels:any={sw:{title:'Dashboard ya Mkopaji',welcome:'Karibu',subtitle:'Dhibiti maombi, mikopo, marejesho na nyaraka zako.',loans:'Mikopo yangu',total:'Jumla ya mikopo',active:'Mikopo inayoendelea',paid:'Imelipwa',pending:'Inasubiri',amount:'Kiasi',status:'Hali',actions:'Vitendo',pay:'Lipa',schedule:'Ratiba',agreement:'Mkataba PDF',profile:'Wasifu',logout:'Toka',empty:'Huna mikopo bado.',refresh:'Onyesha upya'},en:{title:'Borrower Dashboard',welcome:'Welcome',subtitle:'Manage your applications, loans, repayments and documents.',loans:'My loans',total:'Total loans',active:'Active loans',paid:'Paid',pending:'Pending',amount:'Amount',status:'Status',actions:'Actions',pay:'Pay',schedule:'Schedule',agreement:'PDF agreement',profile:'Profile',logout:'Logout',empty:'You have no loans yet.',refresh:'Refresh'}};

export default function BorrowerPage(){
 const router=useRouter(); const {lang}=useLanguage(); const t=labels[lang];
 const [user,setUser]=useState<any>(null),[loans,setLoans]=useState<any[]>([]),[loading,setLoading]=useState(true),[err,setErr]=useState('');
 useEffect(()=>{const token=localStorage.getItem('token'),raw=localStorage.getItem('user');if(!token||!raw){router.push('/login');return}const u=JSON.parse(raw);if(u.role!=='BORROWER'){router.push('/dashboard');return}setUser(u);load();},[router]);
 const load=async()=>{setLoading(true);try{const r=await loanAPI.byBorrower();setLoans(Array.isArray(unwrap(r))?unwrap(r):[]);setErr('')}catch(e:any){setErr(e?.response?.data?.message||'Failed to load loans')}finally{setLoading(false)}};
 const stats=useMemo(()=>({total:loans.length,active:loans.filter(x=>['APPROVED','DISBURSED'].includes(x.status)).length,paid:loans.filter(x=>x.status==='PAID').length,pending:loans.filter(x=>x.status==='PENDING').length}),[loans]);
 const logout=()=>{localStorage.clear();document.cookie='token=; path=/; max-age=0';router.push('/login')};
 if(!user)return null;
 return <div className="min-h-screen bg-slate-50 dark:bg-slate-950 text-slate-900 dark:text-white">
  <nav className="sticky top-0 z-40 border-b bg-white/95 px-4 py-3 backdrop-blur dark:border-slate-800 dark:bg-slate-900/95"><div className="mx-auto flex max-w-7xl items-center justify-between">
   <Link href="/" className="flex items-center gap-3"><div className="flex h-10 w-10 items-center justify-center rounded-xl bg-blue-600 text-xl text-white">💰</div><div><b>JmkLoanApp</b><div className="text-[10px] text-slate-500">{t.title}</div></div></Link>
   <div className="flex items-center gap-2"><LanguageSwitcher/><Link href="/profile" className="hidden rounded-xl px-3 py-2 text-sm font-semibold hover:bg-slate-100 sm:block">{t.profile}</Link><button onClick={logout} className="flex items-center gap-2 rounded-xl px-3 py-2 text-sm font-semibold text-red-600"><LogOut className="h-4 w-4"/>{t.logout}</button></div>
  </div></nav>
  <main className="mx-auto max-w-7xl space-y-6 p-4 sm:p-6">
   <section className="rounded-3xl bg-gradient-to-br from-blue-700 to-slate-900 p-6 text-white shadow-xl"><p className="text-sm text-blue-100">{t.welcome}</p><h1 className="text-3xl font-black">{user.fullName}</h1><p className="mt-2 text-sm text-blue-100">{t.subtitle}</p></section>
   <section className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">{[[t.total,stats.total,FileText],[t.active,stats.active,WalletCards],[t.pending,stats.pending,Clock3],[t.paid,stats.paid,CheckCircle2]].map(([x,v,I]:any)=><div className="rounded-2xl border bg-white p-5 shadow-sm dark:border-slate-800 dark:bg-slate-900" key={x}><I className="h-5 w-5 text-blue-600"/><div className="mt-3 text-3xl font-black">{v}</div><div className="text-sm text-slate-500">{x}</div></div>)}</section>
   {err&&<div className="rounded-2xl border border-red-200 bg-red-50 p-4 text-sm text-red-700">{err}</div>}
   <section className="rounded-3xl border bg-white shadow-sm dark:border-slate-800 dark:bg-slate-900"><div className="flex items-center justify-between border-b p-5 dark:border-slate-800"><div><h2 className="font-black">{t.loans}</h2><p className="text-sm text-slate-500">{user.email}</p></div><button onClick={load} className="rounded-xl border p-2"><RefreshCw className="h-4 w-4"/></button></div>
   {loading?<div className="p-10 text-center text-sm text-slate-500">Loading...</div>:loans.length===0?<div className="p-10 text-center text-sm text-slate-500">{t.empty}</div>:<div className="divide-y dark:divide-slate-800">{loans.map(l=><div key={l.id} className="flex flex-col gap-4 p-5 lg:flex-row lg:items-center lg:justify-between"><div><div className="flex items-center gap-2"><b>Loan #{l.id}</b><span className="rounded-full bg-slate-100 px-2 py-1 text-xs font-bold dark:bg-slate-800">{l.status}</span></div><p className="mt-2 text-sm text-slate-500">{t.amount}: <b>{money(l.amount)}</b></p></div><div className="flex flex-wrap gap-2"><Link href={'/payments?loanId='+l.id} className="rounded-xl bg-blue-600 px-3 py-2 text-xs font-bold text-white"><CreditCard className="mr-1 inline h-3.5 w-3.5"/>{t.pay}</Link><Link href={'/agreements?loanId='+l.id} className="rounded-xl border px-3 py-2 text-xs font-bold"><FileText className="mr-1 inline h-3.5 w-3.5"/>{t.agreement}</Link><Link href={'/signatures?loanId='+l.id} className="rounded-xl border px-3 py-2 text-xs font-bold"><ShieldCheck className="mr-1 inline h-3.5 w-3.5"/>{t.schedule}</Link></div></div>)}</div>}</section>
  </main>
 </div>
}