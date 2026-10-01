'use client';

import {useEffect,useState} from 'react';
import {useRouter} from 'next/navigation';
import {FileSignature,RefreshCw,ShieldCheck} from 'lucide-react';
import {loanAPI,signatureAPI,agreementAPI} from '@/lib/api';

const money=(v:any)=>new Intl.NumberFormat('sw-TZ',{style:'currency',currency:'TZS',maximumFractionDigits:0}).format(Number(v||0));

export default function SignaturesPage(){
 const router=useRouter();const [loans,setLoans]=useState<any[]>([]),[loanId,setLoanId]=useState(''),[type,setType]=useState('BORROWER'),[role,setRole]=useState(''),[msg,setMsg]=useState(''),[busy,setBusy]=useState(false),[ack,setAck]=useState(false),[canvas,setCanvas]=useState<HTMLCanvasElement|null>(null),[drawing,setDrawing]=useState(false);
 const load=async(u:any)=>{try{const r=u.role==='LENDER'||u.role==='ADMIN'?await loanAPI.byLender():await loanAPI.byBorrower();setLoans(r.data?.data??[])}catch(e:any){setMsg(e?.response?.data?.message||'Imeshindikana kupakia loans')}};
 useEffect(()=>{const q=new URLSearchParams(window.location.search).get('loanId');if(q)setLoanId(q);const token=localStorage.getItem('token'),raw=localStorage.getItem('user');if(!token||!raw){router.push('/login');return}const u=JSON.parse(raw);if(!['BORROWER','LENDER','ADMIN'].includes(u.role)){router.push(u.role==='BURSER'?'/bursar':'/login');return;}setRole(u.role);setType(u.role==='LENDER'||u.role==='ADMIN'?'LENDER':'BORROWER');load(u)},[router]);
 const selected=loans.find(l=>String(l.id)===String(loanId));
 const pos=(e:any)=>{if(!canvas)return{x:0,y:0};const r=canvas.getBoundingClientRect();return{x:(e.clientX-r.left)*(canvas.width/r.width),y:(e.clientY-r.top)*(canvas.height/r.height)}};
 const start=(e:any)=>{if(!canvas)return;setDrawing(true);const p=pos(e);const ctx=canvas.getContext('2d')!;ctx.beginPath();ctx.moveTo(p.x,p.y)};
 const move=(e:any)=>{if(!drawing||!canvas)return;const p=pos(e);const ctx=canvas.getContext('2d')!;ctx.lineWidth=3;ctx.lineCap='round';ctx.lineTo(p.x,p.y);ctx.stroke()};
 const clear=()=>{if(canvas)canvas.getContext('2d')!.clearRect(0,0,canvas.width,canvas.height)};
 const openPdf=async(download=false)=>{if(!loanId)return;try{const r=download?await agreementAPI.download(Number(loanId)):await agreementAPI.preview(Number(loanId));const url=URL.createObjectURL(r.data);if(download){const a=document.createElement('a');a.href=url;a.download='loan-'+loanId+'-agreement.pdf';a.click()}else window.open(url,'_blank','noopener,noreferrer');setTimeout(()=>URL.revokeObjectURL(url),10000)}catch(e:any){setMsg('Mkataba haujapatikana au huna ruhusa.')}};
 const save=async()=>{if(!loanId)return setMsg('Chagua loan kwanza.');if(!selected)return setMsg('Loan haijapatikana kwenye akaunti yako.');if(selected.status!=='APPROVED')return setMsg('Sahihi inaruhusiwa baada ya lender ku-approve loan.');if(!ack)return setMsg('Thibitisha kuwa umeisoma na umeelewa agreement.');if(!canvas)return;setBusy(true);setMsg('');try{await signatureAPI.create({loanId:Number(loanId),signatureData:canvas.toDataURL('image/png'),signatureType:type,deviceInfo:navigator.userAgent});setMsg('Sahihi imehifadhiwa kwenye loan agreement.');clear()}catch(e:any){setMsg(e?.response?.data?.message||'Imeshindikana kuhifadhi sahihi')}finally{setBusy(false)}};

 return <main className="min-h-screen bg-slate-50 p-4 dark:bg-slate-950 sm:p-6"><div className="mx-auto max-w-5xl space-y-5">
  <section className="rounded-3xl bg-gradient-to-br from-blue-700 to-slate-950 p-6 text-white"><button onClick={()=>router.push(role==='LENDER'?'/lender':role==='ADMIN'?'/admin/dashboard':'/borrower')} className="mb-4 rounded-xl border border-white/30 px-4 py-2 text-sm font-bold">← Rudi nyuma</button><h1 className="text-3xl font-black">Saini Agreement</h1><p className="mt-2 text-sm text-blue-100">Role yako: <b>{type}</b>. Mfumo haukuruhusu kuchagua role tofauti na akaunti yako.</p></section>
  {msg&&<div className="rounded-2xl border border-blue-200 bg-blue-50 p-4 text-sm font-semibold text-blue-800 dark:border-blue-900 dark:bg-blue-950/30">{msg}</div>}
  <section className="grid gap-5 lg:grid-cols-[.9fr_1.1fr]">
   <div className="space-y-4 rounded-3xl border bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
    <label className="block text-sm font-bold">Chagua loan<select value={loanId} onChange={e=>setLoanId(e.target.value)} className="mt-2 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"><option value="">Chagua</option>{loans.map(l=><option key={l.id} value={l.id}>Loan #{l.id} · {money(l.amount)} · {l.status}</option>)}</select></label>
    {selected&&<div className="space-y-2 rounded-2xl bg-slate-50 p-4 text-sm dark:bg-slate-950"><div><span className="text-slate-500">Mkopaji</span><b className="ml-2">{selected.borrower?.fullName||'—'}</b></div><div><span className="text-slate-500">Kiasi cha loan</span><b className="ml-2">{money(selected.amount)}</b></div>{role==='GUARANTOR'&&<div className="rounded-xl bg-amber-50 p-3 font-bold text-amber-800">Liability yako: {money(selected.guaranteedAmount)} · Status ya udhamini: {selected.guarantorStatus}</div>}<div><span className="text-slate-500">Status</span><b className="ml-2">{selected.status}</b></div></div>}
    {selected&&<div className="flex flex-wrap gap-2"><button onClick={()=>openPdf(false)} className="rounded-xl border px-3 py-2 text-sm font-bold">Soma Agreement</button><button onClick={()=>openPdf(true)} className="rounded-xl border px-3 py-2 text-sm font-bold">Download PDF</button></div>}
    {selected&&<label className="flex items-start gap-3 rounded-2xl border p-4 text-sm dark:border-slate-800"><input type="checkbox" checked={ack} onChange={e=>setAck(e.target.checked)} className="mt-1 h-5 w-5"/><span>Nimesoma na nimeelewa mkataba wa Loan #{selected.id}, kiasi cha {money(selected.amount)}, na wajibu wangu kama {type}.</span></label>}
   </div>
   <div className="rounded-3xl border bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900"><div className="flex items-center gap-3"><ShieldCheck className="text-blue-600"/><div><h2 className="font-black">Sahihi yako</h2><p className="text-xs text-slate-500">Chora sahihi ndani ya kisanduku.</p></div></div><canvas ref={setCanvas} width={900} height={340} onPointerDown={start} onPointerMove={move} onPointerUp={()=>setDrawing(false)} onPointerLeave={()=>setDrawing(false)} className="mt-5 h-72 w-full touch-none rounded-2xl border-2 border-dashed bg-white"/><div className="mt-3 flex gap-2"><button type="button" onClick={clear} className="rounded-xl border px-4 py-2 text-sm font-bold">Futa</button><button disabled={busy||!selected} onClick={save} className="flex-1 rounded-xl bg-blue-600 px-4 py-2 font-black text-white disabled:opacity-50">{busy?<RefreshCw className="mx-auto h-4 w-4 animate-spin"/>:<><FileSignature className="mr-2 inline h-4 w-4"/>Hifadhi Sahihi</>}</button></div></div>
  </section>
  <div className="rounded-2xl border bg-white p-4 text-sm text-slate-600 dark:border-slate-800 dark:bg-slate-900 dark:text-slate-300"><b>Mdhamini:</b> hana account ya mfumo. Lender anaweza kutumia onsite capture au kutuma secure remote signing link inayohusishwa na Loan ID.</div>
 </div></main>;
}
