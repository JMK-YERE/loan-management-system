'use client';

import { useEffect, useState } from 'react';
import { Download, Eye, FileSignature, FileText } from 'lucide-react';
import Link from 'next/link';
import { agreementAPI, loanAPI } from '@/lib/api';

export default function AgreementsPage(){
 const [loans,setLoans]=useState<any[]>([]); const [msg,setMsg]=useState(''); const [role,setRole]=useState('');
 useEffect(()=>{(async()=>{try{const u=JSON.parse(localStorage.getItem('user')||'{}');setRole(u.role||'');const r=u.role==='LENDER'||u.role==='ADMIN'?await loanAPI.byLender():await loanAPI.byBorrower();setLoans(r.data?.data||r.data||[]);}catch(e){setMsg('Imeshindikana kupakia mikopo.')}})()},[]);
 const open=async(id:number,download=false)=>{try{const r=download?await agreementAPI.download(id):await agreementAPI.preview(id);const url=URL.createObjectURL(r.data);if(download){const a=document.createElement('a');a.href=url;a.download='loan-'+id+'-signed-agreement.pdf';a.click();}else window.open(url,'_blank','noopener,noreferrer');setTimeout(()=>URL.revokeObjectURL(url),10000);}catch(e:any){setMsg(e?.response?.data?.message||'Mkataba haujapatikana au huna ruhusa.')}}
 return <main className="min-h-screen bg-slate-50 p-6 dark:bg-slate-950"><div className="mx-auto max-w-5xl space-y-6">
  <section className="rounded-3xl bg-gradient-to-br from-blue-700 to-slate-950 p-6 text-white shadow-xl"><div className="flex items-center gap-3"><div className="rounded-xl bg-white/10 p-3"><FileText/></div><div><h1 className="text-2xl font-black">Loan Agreements</h1><p className="text-sm text-blue-100">Soma mkataba, hakiki masharti na pakua nakala ya kumbukumbu.</p></div></div></section>
  {msg&&<div className="rounded-2xl border border-red-200 bg-red-50 p-4 text-sm text-red-700">{msg}</div>}
  <section className="space-y-3">{loans.length===0?<div className="rounded-2xl bg-white p-8 text-center text-slate-500 dark:bg-slate-900">Hakuna mikopo.</div>:loans.map(l=><div key={l.id} className="rounded-2xl bg-white p-5 shadow-sm dark:bg-slate-900"><div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between"><div><b>Loan #{l.id}</b><p className="text-sm text-slate-500">TZS {l.amount} · {l.status}</p></div><div className="flex flex-wrap gap-2"><button onClick={()=>open(l.id,false)} className="flex items-center gap-2 rounded-xl border px-4 py-2 text-sm font-bold"><Eye className="h-4 w-4"/> Soma</button><button onClick={()=>open(l.id,true)} className="flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-sm font-bold text-white"><Download className="h-4 w-4"/> Download</button><Link href={'/signatures?loanId='+l.id} className="flex items-center gap-2 rounded-xl border px-4 py-2 text-sm font-bold"><FileSignature className="h-4 w-4"/> Saini</Link></div></div></div>)}</section>
  <Link href={role==='LENDER'||role==='ADMIN'?'/lender':'/borrower'} className="font-bold text-blue-600">← Rudi Dashboard</Link>
 </div></main>;
}
