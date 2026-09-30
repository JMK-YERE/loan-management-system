'use client';

import { useEffect, useState } from 'react';
import { Download, Eye, FileSignature, FileText, RefreshCw, ShieldCheck } from 'lucide-react';
import Link from 'next/link';
import { agreementAPI, loanAPI } from '@/lib/api';

const money=(v:any)=>new Intl.NumberFormat('sw-TZ',{style:'currency',currency:'TZS',maximumFractionDigits:0}).format(Number(v||0));

export default function AgreementsPage(){
 const [loans,setLoans]=useState<any[]>([]),[msg,setMsg]=useState(''),[role,setRole]=useState(''),[loading,setLoading]=useState(true);

 const load=async()=>{
  setLoading(true);
  try{
   const u=JSON.parse(localStorage.getItem('user')||'{}');setRole(u.role||'');
   const r=u.role==='LENDER'||u.role==='ADMIN'?await loanAPI.byLender():await loanAPI.byBorrower();
   setLoans(r.data?.data||r.data||[]);
  }catch(e:any){setMsg(e?.response?.data?.message||'Imeshindikana kupakia mikopo.')}
  finally{setLoading(false)}
 };
 useEffect(()=>{load()},[]);

 const open=async(id:number,download=false)=>{
  try{
   const r=download?await agreementAPI.download(id):await agreementAPI.preview(id);
   const url=URL.createObjectURL(r.data);
   if(download){const a=document.createElement('a');a.href=url;a.download='loan-'+id+'-agreement.pdf';a.click();}
   else window.open(url,'_blank','noopener,noreferrer');
   setTimeout(()=>URL.revokeObjectURL(url),10000);
  }catch(e:any){setMsg(e?.response?.data?.message||'Mkataba haujapatikana au huna ruhusa.')}
 };

 const home=role==='LENDER'||role==='ADMIN'?'/lender':role==='GUARANTOR'?'/guarantor':'/borrower';

 return <main className="min-h-screen bg-slate-50 p-4 sm:p-6 dark:bg-slate-950">
  <div className="mx-auto max-w-5xl space-y-6">
   <section className="rounded-3xl bg-gradient-to-br from-blue-700 via-indigo-700 to-slate-950 p-6 text-white shadow-xl">
    <div className="flex flex-wrap items-center justify-between gap-4">
     <div><p className="text-sm text-blue-100">JmkLoanApp · Documents</p><h1 className="text-3xl font-black">Loan Agreements</h1><p className="mt-2 text-sm text-blue-100">Mikataba yako iliyohusishwa na loans. Soma, pakua na nenda moja kwa moja kwenye e-signature.</p></div>
     <button onClick={load} className="rounded-xl bg-white/10 px-4 py-2 text-sm font-bold"><RefreshCw className="mr-2 inline h-4 w-4"/>Refresh</button>
    </div>
   </section>

   <div className="flex flex-wrap gap-2">
    <Link href={home} className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">← Dashboard</Link>
    {role==='BORROWER'&&<Link href="/loan-offer" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">Loan Offers</Link>}
    <Link href="/signatures" className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-black text-white"><FileSignature className="mr-1 inline h-4 w-4"/>E-Signatures</Link>
    <Link href="/repayments" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">Repayment Schedule</Link>
   </div>

   {msg&&<div className="rounded-2xl border border-red-200 bg-red-50 p-4 text-sm font-semibold text-red-700">{msg}</div>}

   {loading?<div className="rounded-3xl bg-white p-10 text-center text-slate-500 dark:bg-slate-900">Inapakia...</div>:
    loans.length===0?<div className="rounded-3xl bg-white p-10 text-center text-slate-500 dark:bg-slate-900">Hakuna loan inayopatikana kwa akaunti hii bado.</div>:
    <section className="space-y-4">{loans.map(l=>{
     const actionable=['APPROVED','DISBURSED','ACTIVE','PAID','DEFAULTED'].includes(l.status);
     return <article key={l.id} className="rounded-3xl border bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
      <div className="flex flex-wrap items-start justify-between gap-3">
       <div><div className="flex items-center gap-2"><b className="text-xl">Loan #{l.id}</b><span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-black dark:bg-slate-800">{l.status}</span></div><p className="mt-1 text-sm text-slate-500">{l.borrower?.fullName||'Borrower'} · {money(l.amount)}</p></div>
       <div className="rounded-xl bg-blue-50 p-3 text-blue-700 dark:bg-blue-950/30"><ShieldCheck className="h-5 w-5"/></div>
      </div>
      <div className="mt-5 grid gap-3 sm:grid-cols-3">
       <div className="rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><div className="text-xs text-slate-500">Loan amount</div><b>{money(l.amount)}</b></div>
       <div className="rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><div className="text-xs text-slate-500">Status</div><b>{l.status}</b></div>
       <div className="rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><div className="text-xs text-slate-500">Loan ID</div><b>#{l.id}</b></div>
      </div>
      <div className="mt-5 flex flex-wrap gap-2">
       <button onClick={()=>open(l.id,false)} className="flex items-center gap-2 rounded-xl border px-4 py-2 text-sm font-black"><Eye className="h-4 w-4"/>Soma Agreement</button>
       <button onClick={()=>open(l.id,true)} className="flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-sm font-black text-white"><Download className="h-4 w-4"/>Download PDF</button>
       {actionable&&<Link href={'/signatures?loanId='+l.id} className="flex items-center gap-2 rounded-xl border border-blue-300 px-4 py-2 text-sm font-black text-blue-700"><FileSignature className="h-4 w-4"/>Saini Agreement</Link>}
       <Link href={'/repayments?loanId='+l.id} className="rounded-xl border px-4 py-2 text-sm font-black">Ratiba ya Marejesho</Link>
      </div>
     </article>
    })}</section>}
  </div>
 </main>
}
