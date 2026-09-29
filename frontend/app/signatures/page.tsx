'use client';

import { useEffect, useRef, useState } from 'react';
import { FileSignature, RefreshCw, ShieldCheck } from 'lucide-react';
import { loanAPI, signatureAPI } from '@/lib/api';

export default function SignaturesPage(){
 const canvasRef=useRef<HTMLCanvasElement|null>(null); const drawing=useRef(false);
 const [loans,setLoans]=useState<any[]>([]); const [loanId,setLoanId]=useState(''); const [type,setType]=useState('BORROWER'); const [msg,setMsg]=useState(''); const [busy,setBusy]=useState(false);
 useEffect(()=>{(async()=>{try{const u=JSON.parse(localStorage.getItem('user')||'{}');const r=u.role==='LENDER'?await loanAPI.byLender():await loanAPI.byBorrower();setLoans(r.data?.data||r.data||[]);}catch(e){setMsg('Imeshindikana kupakia mikopo.')}})()},[]);
 const pos=(e:any)=>{const c=canvasRef.current!;const r=c.getBoundingClientRect();return {x:(e.clientX-r.left)*(c.width/r.width),y:(e.clientY-r.top)*(c.height/r.height)}};
 const start=(e:any)=>{drawing.current=true;const p=pos(e);const c=canvasRef.current!;const ctx=c.getContext('2d')!;ctx.beginPath();ctx.moveTo(p.x,p.y)};
 const move=(e:any)=>{if(!drawing.current)return;const p=pos(e);const ctx=canvasRef.current!.getContext('2d')!;ctx.lineWidth=3;ctx.lineCap='round';ctx.lineTo(p.x,p.y);ctx.stroke()};
 const end=()=>{drawing.current=false};
 const clear=()=>{const c=canvasRef.current!;c.getContext('2d')!.clearRect(0,0,c.width,c.height)};
 const save=async()=>{if(!loanId)return setMsg('Chagua mkopo.');const c=canvasRef.current!;const data=c.toDataURL('image/png');setBusy(true);setMsg('');try{await signatureAPI.create({loanId:Number(loanId),signatureData:data,signatureType:type,deviceInfo:navigator.userAgent});setMsg('Sahihi imehifadhiwa kwenye mkataba.');clear();}catch(e:any){setMsg(e?.response?.data?.message||'Imeshindikana kuhifadhi sahihi.')}finally{setBusy(false)}};
 return <main className="min-h-screen bg-slate-50 p-6 dark:bg-slate-950"><div className="mx-auto max-w-4xl space-y-6">
  <section className="rounded-3xl bg-white p-6 shadow-sm dark:bg-slate-900"><div className="flex items-center gap-3"><div className="rounded-xl bg-blue-50 p-3 text-blue-600"><FileSignature/></div><div><h1 className="text-2xl font-black">Digital E-Signature</h1><p className="text-sm text-slate-500">Chora sahihi yako na ihifadhiwe pamoja na taarifa za kifaa na muda.</p></div></div></section>
  <section className="grid gap-6 rounded-3xl bg-white p-6 shadow-sm dark:bg-slate-900 md:grid-cols-2">
   <div className="space-y-4"><select value={loanId} onChange={e=>setLoanId(e.target.value)} className="w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"><option value="">Chagua mkopo</option>{loans.map(l=><option key={l.id} value={l.id}>Loan #{l.id} · TZS {l.amount}</option>)}</select>
   <select value={type} onChange={e=>setType(e.target.value)} className="w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950">{['BORROWER','LENDER','GUARANTOR','WITNESS','LAWYER'].map(x=><option key={x}>{x}</option>)}</select>
   <div className="rounded-2xl bg-blue-50 p-4 text-sm text-blue-800"><ShieldCheck className="mb-2 h-5 w-5"/>Sahihi huunganishwa na akaunti yako na loan ID; usisaini mkataba usiouelewa.</div></div>
   <div><canvas ref={canvasRef} width={800} height={320} onPointerDown={start} onPointerMove={move} onPointerUp={end} onPointerLeave={end} className="h-64 w-full touch-none rounded-2xl border-2 border-dashed bg-white"/><div className="mt-3 flex gap-2"><button onClick={clear} className="rounded-xl border px-4 py-2">Futa</button><button disabled={busy} onClick={save} className="flex flex-1 items-center justify-center gap-2 rounded-xl bg-blue-600 px-4 py-2 font-bold text-white">{busy?<RefreshCw className="animate-spin h-4 w-4"/>:<FileSignature className="h-4 w-4"/>} Hifadhi Sahihi</button></div></div>
  </section>{msg&&<div className="rounded-2xl border bg-white p-4 text-sm dark:bg-slate-900">{msg}</div>}
 </div></main>;
}
