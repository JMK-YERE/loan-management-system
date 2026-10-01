'use client';

import {useEffect,useState} from 'react';
import {useRouter} from 'next/navigation';
import Link from 'next/link';
import {Download,LockKeyhole,RefreshCw} from 'lucide-react';
import {managementFinanceAPI} from '@/lib/api';

const money=(v:any)=>new Intl.NumberFormat('sw-TZ',{style:'currency',currency:'TZS',maximumFractionDigits:0}).format(Number(v||0));

export default function FinancialStatementPage(){
 const router=useRouter();const [s,setS]=useState<any>(null),[msg,setMsg]=useState(''),[loading,setLoading]=useState(true);
 const load=async()=>{setLoading(true);try{const r=await managementFinanceAPI.statement();setS(r.data?.data??r.data)}catch(e:any){setMsg(e?.response?.data?.message||'Huna ruhusa au taarifa haipatikani.')}finally{setLoading(false)}};
 useEffect(()=>{const raw=localStorage.getItem('user'),token=localStorage.getItem('token');if(!raw||!token){router.push('/login');return;}const u=JSON.parse(raw);if(!['DIRECTOR','ADMIN'].includes(u.role)){router.push(u.role==='BURSER'?'/bursar':u.role==='LENDER'?'/lender':'/dashboard');return;}load()},[router]);
 const exportCsv=()=>{if(!s)return;const rows=[['Metric','Value'],['As of',s.asOf],['Loans',s.loanCount],['Active loans',s.activeLoans],['Paid loans',s.paidLoans],['Defaulted loans',s.defaultedLoans],['Disbursed value',s.disbursedValue],['Contracted receivable',s.contractedReceivable],['Payments received',s.paymentsReceived],['Pending payments',s.pendingPayments],['Outstanding',s.outstanding],['Overdue amount',s.overdueAmount]];const csv=rows.map(r=>r.map(v=>String(v??'')).join(',')).join('\n');const a=document.createElement('a');a.href=URL.createObjectURL(new Blob([csv],{type:'text/csv'}));a.download='jmk-management-financial-statement.csv';a.click();};
 return <main className="min-h-screen bg-slate-50 p-4 dark:bg-slate-950 sm:p-6"><div className="mx-auto max-w-6xl space-y-5">
  <section className="rounded-3xl bg-gradient-to-br from-slate-900 to-blue-900 p-7 text-white"><div className="flex flex-wrap items-start justify-between gap-3"><div><p className="text-sm text-blue-200">Management View</p><h1 className="text-3xl font-black">Financial Statement</h1><p className="mt-2 text-sm text-blue-100">Taarifa ya kifedha ya kusoma tu. Director anaona figures lakini hana controls za kubadilisha loan, payment au settings.</p></div><LockKeyhole className="h-8 w-8"/></div></section>
  {msg&&<div className="rounded-2xl border bg-white p-4 text-sm text-red-700 dark:bg-slate-900">{msg}</div>}
  <div className="flex flex-wrap gap-2"><button onClick={load} className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900"><RefreshCw className="mr-1 inline h-4 w-4"/>Refresh</button><button onClick={exportCsv} disabled={!s} className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-black text-white disabled:opacity-50"><Download className="mr-1 inline h-4 w-4"/>Download CSV</button><Link href="/dashboard" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">Dashboard</Link></div>
  {loading?<div className="rounded-3xl border bg-white p-10 text-center dark:bg-slate-900">Inapakia...</div>:s&&<><div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">{[['Disbursed value',s.disbursedValue],['Contracted receivable',s.contractedReceivable],['Payments received',s.paymentsReceived],['Outstanding',s.outstanding],['Overdue amount',s.overdueAmount],['Pending payments',s.pendingPayments],['Active loans',s.activeLoans],['Defaulted loans',s.defaultedLoans]].map(([k,v]:any)=><div key={k} className="rounded-2xl border bg-white p-5 shadow-sm dark:border-slate-800 dark:bg-slate-900"><div className="text-xs text-slate-500">{k}</div><div className="mt-2 text-2xl font-black">{typeof v==='number'?v:money(v)}</div></div>)}</div><div className="rounded-3xl border border-amber-200 bg-amber-50 p-5 text-sm dark:border-amber-900 dark:bg-amber-950/30">Read-only: hakuna button ya ku-edit, approve, delete au kubadilisha financial data hapa.</div></>}
 </div></main>;
}