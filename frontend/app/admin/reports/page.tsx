'use client';

import { useEffect, useState } from 'react';
import { Download, RefreshCw } from 'lucide-react';
import { adminAPI } from '@/lib/api';
import LanguageSwitcher from '@/components/LanguageSwitcher';
import { useLanguage } from '@/lib/useLanguage';

export default function ReportsPage(){
 const {lang}=useLanguage(); const en=lang==='en'; const [s,setS]=useState<any>(); const [loading,setLoading]=useState(true);
 const load=async()=>{setLoading(true);try{const r=await adminAPI.stats();setS(r.data.data);}finally{setLoading(false);}};
 useEffect(()=>{load()},[]);
 const money=(v:any)=>new Intl.NumberFormat('sw-TZ',{style:'currency',currency:'TZS',maximumFractionDigits:0}).format(Number(v||0));
 const exportReport=()=>{if(!s)return; const rows=[['Metric','Value'],['Total users',s.totalUsers],['Active users',s.activeUsers],['Pending users',s.pendingUsers],['Total loans',s.totalLoans],['Pending loans',s.pendingLoans],['Approved/disbursed',s.approvedLoans],['Paid loans',s.paidLoans],['Defaulted loans',s.defaultedLoans],['Portfolio',s.portfolioAmount],['Successful repayments',s.successfulRepayments],['Total payments',s.totalPayments]]; const csv=rows.map(r=>r.map(v=>`"${String(v??'').replaceAll('"','""')}"`).join(',')).join('\n'); const a=document.createElement('a');a.href=URL.createObjectURL(new Blob([csv],{type:'text/csv'}));a.download='jmkloan-report.csv';a.click();URL.revokeObjectURL(a.href);};
 return <div className="p-4 sm:p-6 lg:p-8"><div className="mb-8 flex flex-wrap items-center justify-between gap-4"><div><p className="text-sm font-bold text-blue-600">JmkLoanApp</p><h1 className="text-3xl font-black">{en?'Reports':'Ripoti'}</h1><p className="mt-1 text-slate-500">{en?'Live operational report from the database.':'Ripoti ya shughuli kutoka kwenye database.'}</p></div><div className="flex gap-2"><LanguageSwitcher/><button onClick={load} className="rounded-xl border p-2.5"><RefreshCw className="h-4 w-4"/></button><button onClick={exportReport} disabled={!s} className="flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-sm font-bold text-white disabled:opacity-50"><Download className="h-4 w-4"/>{en?'Export CSV':'Pakua CSV'}</button></div></div>{loading?<div className="rounded-3xl border p-10 text-center text-slate-500">Loading...</div>:<div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">{[['Users',s?.totalUsers],['Active',s?.activeUsers],['Pending users',s?.pendingUsers],['Loans',s?.totalLoans],['Portfolio',money(s?.portfolioAmount)],['Repayments',money(s?.successfulRepayments)],['Pending loans',s?.pendingLoans],['Paid loans',s?.paidLoans],['Defaulted',s?.defaultedLoans]].map(([k,v])=><div key={k} className="rounded-2xl border bg-white p-5 shadow-sm dark:border-slate-800 dark:bg-slate-900"><p className="text-sm text-slate-500">{k}</p><p className="mt-2 text-2xl font-black">{v}</p></div>)}</div>}</div>;
}
