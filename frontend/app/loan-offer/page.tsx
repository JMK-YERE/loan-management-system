'use client';

import {useEffect,useState} from 'react';
import {useRouter} from 'next/navigation';
import Link from 'next/link';
import {CheckCircle2,Clock3,Download,Eye,FileSignature,RefreshCw,ShieldCheck} from 'lucide-react';
import api from '@/lib/api';

const money=(v:any)=>new Intl.NumberFormat('sw-TZ',{style:'currency',currency:'TZS',maximumFractionDigits:0}).format(Number(v||0));
const unwrap=(r:any)=>r?.data?.data??r?.data??[];

export default function LoanOfferPage(){
 const router=useRouter();
 const [apps,setApps]=useState<any[]>([]),[msg,setMsg]=useState(''),[read,setRead]=useState<Record<number,boolean>>({}),[busy,setBusy]=useState<number|null>(null);

 const load=async()=>{
  try{const r=await api.get('/general-loan-applications/mine');setApps(Array.isArray(unwrap(r))?unwrap(r):[]);}
  catch(e:any){setMsg(e?.response?.data?.message||'Imeshindikana kupakia maombi na offers.');}
 };
 useEffect(()=>{load()},[]);

 const accept=async(id:number)=>{
  setBusy(id);setMsg('');
  try{await api.put('/general-loan-applications/'+id+'/accept-offer');setMsg('Offer imekubaliwa kikamilifu. Sasa lender ataendelea na hatua inayofuata.');await load();}
  catch(e:any){setMsg(e?.response?.data?.message||'Imeshindikana kukubali offer.');}
  finally{setBusy(null)}
 };

 const openPdf=async(id:number,download=false)=>{
  try{
   const r=await api.get('/agreements/application/'+id+(download?'/pdf':'/preview'),{responseType:'blob'});
   const url=URL.createObjectURL(r.data);
   if(download){const a=document.createElement('a');a.href=url;a.download='application-'+id+'-loan-offer.pdf';a.click();}
   else window.open(url,'_blank','noopener,noreferrer');
   setTimeout(()=>URL.revokeObjectURL(url),10000);
  }catch(e:any){setMsg(e?.response?.data?.message||'Agreement PDF haikupatikana au huna ruhusa.');}
 };

 const statusLabel=(status:string)=>{
  const map:any={
   SUBMITTED:'Ombi limetumwa',
   UNDER_REVIEW:'Offer iko tayari / inasubiri acceptance',
   APPROVED:'Loan inaandaliwa',
   REJECTED:'Ombi limekataliwa',
   CONVERTED:'Offer imekubaliwa — loan imetengenezwa',
   CANCELLED:'Ombi limefungwa'
  };
  return map[status]||status;
 };

 return <main className="min-h-screen bg-slate-50 p-4 sm:p-6 dark:bg-slate-950">
  <div className="mx-auto max-w-5xl space-y-6">
   <section className="rounded-3xl bg-gradient-to-br from-blue-700 via-indigo-700 to-slate-950 p-6 text-white shadow-xl">
    <div className="flex flex-wrap items-start justify-between gap-4">
     <div><p className="text-sm text-blue-100">JmkLoanApp · Borrower</p><h1 className="mt-1 text-3xl font-black">Loan Offers & Agreements</h1><p className="mt-2 max-w-2xl text-sm text-blue-100">Hapa ndipo unaona offer, mkataba, gharama, ratiba na hatua inayofuata. Taarifa hizi zinatoka kwenye application yako.</p></div>
     <button onClick={load} className="rounded-xl bg-white/10 px-4 py-2 text-sm font-bold"><RefreshCw className="mr-2 inline h-4 w-4"/>Refresh</button>
    </div>
   </section>

   <div className="flex flex-wrap gap-2">
    <Link href="/borrower" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">Dashboard</Link>
    <Link href="/agreements" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">Agreements</Link>
    <Link href="/signatures" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">Saini</Link>
    <Link href="/repayments" className="rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">Ratiba</Link>
   </div>

   {msg&&<div className="rounded-2xl border border-blue-200 bg-blue-50 p-4 text-sm font-semibold text-blue-800 dark:border-blue-900 dark:bg-blue-950/30 dark:text-blue-200">{msg}</div>}

   {apps.length===0&&<div className="rounded-3xl bg-white p-10 text-center text-sm text-slate-500 shadow-sm dark:bg-slate-900">Huna loan offer au application inayopatikana kwa sasa.</div>}

   <div className="space-y-4">
   {apps.map(a=>{
    const offerReady=!!a.product&&['UNDER_REVIEW','APPROVED'].includes(a.status);
    const accepted=!!a.termsAccepted;
    return <section key={a.id} className="rounded-3xl border bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
     <div className="flex flex-wrap items-start justify-between gap-3">
      <div><div className="flex items-center gap-2"><h2 className="text-xl font-black">Application #{a.id}</h2><span className="rounded-full bg-slate-100 px-3 py-1 text-xs font-black dark:bg-slate-800">{statusLabel(a.status)}</span></div><p className="mt-1 text-sm text-slate-500">{a.product?.name||'Loan product bado haijawekwa'}</p></div>
      {accepted?<span className="rounded-xl bg-emerald-50 px-3 py-2 text-sm font-black text-emerald-700"><CheckCircle2 className="mr-1 inline h-4 w-4"/>Accepted</span>:offerReady?<span className="rounded-xl bg-amber-50 px-3 py-2 text-sm font-black text-amber-700"><Clock3 className="mr-1 inline h-4 w-4"/>Action required</span>:null}
     </div>

     <div className="mt-5 grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
      <div className="rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><div className="text-xs text-slate-500">Kiasi cha mkopo</div><b>{money(a.amount)}</b></div>
      <div className="rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><div className="text-xs text-slate-500">Riba</div><b>{money(a.interestSnapshot)}</b></div>
      <div className="rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><div className="text-xs text-slate-500">Ada</div><b>{money(a.processingFeeSnapshot)}</b></div>
      <div className="rounded-2xl bg-emerald-50 p-4 dark:bg-emerald-950/30"><div className="text-xs text-slate-500">Jumla ya kurejesha</div><b>{money(a.totalRepaymentSnapshot)}</b></div>
      <div className="rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><div className="text-xs text-slate-500">Muda</div><b>{a.duration} {a.durationUnit||'DAYS'}</b></div>
      <div className="rounded-2xl bg-slate-50 p-4 dark:bg-slate-950"><div className="text-xs text-slate-500">Purpose</div><b>{a.purpose||'—'}</b></div>
     </div>

     {offerReady&&<div className="mt-5 rounded-2xl border border-blue-200 bg-blue-50 p-5 dark:border-blue-900 dark:bg-blue-950/30">
      <div className="flex items-start gap-3"><ShieldCheck className="mt-1 h-5 w-5 text-blue-600"/><div><b>{accepted?'Offer accepted':'Kabla hujakubali'}</b><p className="mt-1 text-sm text-slate-600 dark:text-slate-300">{accepted?'Acceptance yako imehifadhiwa. Lender sasa ataendelea na approval ya loan; baada ya loan kutengenezwa ndipo e-signature itawezeshwa.': 'Soma PDF kwanza, kagua kiasi/riba/ada/jumla ya marejesho, kisha kubali masharti.'}</p></div></div>
      <div className="mt-4 flex flex-wrap gap-2">
       <button onClick={()=>openPdf(a.id,false)} className="flex items-center gap-2 rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900"><Eye className="h-4 w-4"/>Soma Agreement</button>
       <button onClick={()=>openPdf(a.id,true)} className="flex items-center gap-2 rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900"><Download className="h-4 w-4"/>Download PDF</button>
       {!accepted&&<label className="flex items-center gap-2 rounded-xl border bg-white px-3 py-2 text-sm font-semibold dark:bg-slate-900"><input type="checkbox" checked={!!read[a.id]} onChange={e=>setRead({...read,[a.id]:e.target.checked})}/> Nimesoma na nimeelewa</label>}
       {!accepted&&<button disabled={!read[a.id]||busy===a.id} onClick={()=>accept(a.id)} className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-black text-white disabled:cursor-not-allowed disabled:opacity-50">{busy===a.id?'Inahifadhi...':'✓ Kubali Offer'}</button>}
       {accepted&&<><Link href="/agreements" className="flex items-center gap-2 rounded-xl border bg-white px-4 py-2 text-sm font-black dark:bg-slate-900">📄 Agreements</Link><span className="rounded-xl bg-amber-50 px-4 py-2 text-sm font-bold text-amber-700">Subiri lender aidhinishe na kutengeneza loan</span></>}
      </div>
     </div>}

     {!offerReady&&<div className="mt-5 rounded-2xl border border-slate-200 p-4 text-sm text-slate-600 dark:border-slate-800 dark:text-slate-300">⏳ Application yako ipo <b>{statusLabel(a.status)}</b>. Ukishawekewa offer na lender, buttons za kusoma, kupakua na kukubali zitaonekana hapa.</div>}
    </section>
   })}
   </div>

   <button onClick={()=>router.push('/borrower')} className="font-bold text-blue-600">← Rudi Dashboard</button>
  </div>
 </main>
}
