'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { FileSignature, RefreshCw, ShieldCheck } from 'lucide-react';
import { guarantorAPI, loanAPI, signatureAPI, agreementAPI } from '@/lib/api';

export default function SignaturesPage(){
 const router=useRouter();
 const [canvas,setCanvas]=useState<HTMLCanvasElement|null>(null);
 const [loans,setLoans]=useState<any[]>([]),[loanId,setLoanId]=useState(''),[type,setType]=useState('BORROWER'),[role,setRole]=useState(''),[msg,setMsg]=useState(''),[busy,setBusy]=useState(false);
 const [drawing,setDrawing]=useState(false);
 useEffect(()=>{const q=new URLSearchParams(window.location.search).get('loanId');if(q)setLoanId(q);const token=localStorage.getItem('token'),raw=localStorage.getItem('user');if(!token||!raw){router.push('/login');return;}const u=JSON.parse(raw);setRole(u.role);setType(u.role==='GUARANTOR'?'GUARANTOR':u.role==='LENDER'||u.role==='ADMIN'?'LENDER':'BORROWER');
  (async()=>{try{if(u.role==='GUARANTOR'){const r=await guarantorAPI.mine();const d=r.data?.data??r.data??[];setLoans(Array.isArray(d)?d.map((g:any)=>g.loan).filter(Boolean):[]);}else{const r=u.role==='LENDER'||u.role==='ADMIN'?await loanAPI.byLender():await loanAPI.byBorrower();setLoans(r.data?.data??r.data??[]);}}catch(e:any){setMsg(e?.response?.data?.message||'Imeshindikana kupakia mikopo.')}})();
 },[router]);
 const position=(e:any)=>{if(!canvas)return {x:0,y:0};const r=canvas.getBoundingClientRect();return{x:(e.clientX-r.left)*(canvas.width/r.width),y:(e.clientY-r.top)*(canvas.height/r.height)}};
 const start=(e:any)=>{if(!canvas)return;setDrawing(true);const p=position(e);const ctx=canvas.getContext('2d')!;ctx.beginPath();ctx.moveTo(p.x,p.y)};
 const move=(e:any)=>{if(!drawing||!canvas)return;const p=position(e);const ctx=canvas.getContext('2d')!;ctx.lineWidth=3;ctx.lineCap='round';ctx.lineTo(p.x,p.y);ctx.stroke()};
 const clear=()=>{if(canvas)canvas.getContext('2d')!.clearRect(0,0,canvas.width,canvas.height)};
 const save=async()=>{if(!loanId)return setMsg('Chagua mkopo.');if(!canvas)return;setBusy(true);setMsg('');try{await signatureAPI.create({loanId:Number(loanId),signatureData:canvas.toDataURL('image/png'),signatureType:type,deviceInfo:navigator.userAgent});setMsg('Sahihi imehifadhiwa.');clear();}catch(e:any){setMsg(e?.response?.data?.message||'Imeshindikana kuhifadhi sahihi.')}finally{setBusy(false)}};
 return <main className="min-h-screen bg-slate-50 p-6 dark:bg-slate-950"><div className="mx-auto max-w-4xl space-y-6">
  <section className="rounded-3xl bg-white p-6 shadow-sm dark:bg-slate-900"><div className="flex items-center gap-3"><div className="rounded-xl bg-blue-50 p-3 text-blue-600"><FileSignature/></div><div><h1 className="text-2xl font-black">Digital E-Signature</h1><p className="text-sm text-slate-500">Unasaini kama <b>{type}</b>. Mfumo haukuruhusu kuchagua role nyingine.</p></div></div></section>
  {msg&&<div className="rounded-2xl border bg-white p-4 text-sm dark:bg-slate-900">{msg}</div>}
  <section className="grid gap-6 rounded-3xl bg-white p-6 shadow-sm dark:bg-slate-900 md:grid-cols-2">
   <div className="space-y-4"><select value={loanId} onChange={e=>setLoanId(e.target.value)} className="w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"><option value="">Chagua mkopo</option>{loans.map(l=><option key={l.id} value={l.id}>Loan #{l.id} · TZS {l.amount} · {l.status}</option>)}</select>
   <div className="rounded-2xl bg-blue-50 p-4 text-sm text-blue-800"><ShieldCheck className="mb-2 h-5 w-5"/>Sahihi inaruhusiwa baada ya approval. Kwa guarantor, guarantee lazima iwe APPROVED.</div>
   {loanId&&<div className="mt-3 flex flex-wrap gap-2"><button onClick={async()=>{try{const r=await agreementAPI.preview(Number(loanId));const url=URL.createObjectURL(r.data);window.open(url,'_blank','noopener,noreferrer');setTimeout(()=>URL.revokeObjectURL(url),10000)}catch(e:any){setMsg('Mkataba haujapatikana.')}}} className="rounded-xl border bg-white px-3 py-2 text-sm font-bold">Soma Mkataba</button><button onClick={async()=>{try{const r=await agreementAPI.download(Number(loanId));const url=URL.createObjectURL(r.data);const a=document.createElement('a');a.href=url;a.download='loan-'+loanId+'-agreement.pdf';a.click();setTimeout(()=>URL.revokeObjectURL(url),10000)}catch(e:any){setMsg('PDF haijapatikana.')}}} className="rounded-xl border bg-white px-3 py-2 text-sm font-bold">Download</button></div>}
   </div>
   <div><canvas ref={setCanvas} width={800} height={320} onPointerDown={start} onPointerMove={move} onPointerUp={()=>setDrawing(false)} onPointerLeave={()=>setDrawing(false)} className="h-64 w-full touch-none rounded-2xl border-2 border-dashed bg-white"/><div className="mt-3 flex gap-2"><button onClick={clear} className="rounded-xl border px-4 py-2">Futa</button><button disabled={busy} onClick={save} className="flex flex-1 items-center justify-center gap-2 rounded-xl bg-blue-600 px-4 py-2 font-bold text-white">{busy?<RefreshCw className="h-4 w-4 animate-spin"/>:<FileSignature className="h-4 w-4"/>} Hifadhi Sahihi</button></div></div>
  </section>
 </div></main>;
}