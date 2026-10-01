'use client';

import {useEffect,useState} from 'react';
import {useRouter} from 'next/navigation';
import {collateralAPI,loanAPI} from '@/lib/api';

export default function CollateralPage(){
 const router=useRouter(); const [loan,setLoan]=useState<any>(null),[type,setType]=useState('GARI'),[description,setDescription]=useState(''),[value,setValue]=useState(''),[photos,setPhotos]=useState<string[]>([]),[status,setStatus]=useState(''),[busy,setBusy]=useState(false);
 const loanId=typeof window!=='undefined'?new URLSearchParams(window.location.search).get('loanId'):null;
 useEffect(()=>{if(!loanId)return;loanAPI.get(Number(loanId)).then(r=>setLoan(r.data?.data??r.data)).catch(e=>setStatus(e?.response?.data?.message||'Mkopo haujapatikana'));},[loanId]);
 const files=(e:any)=>{const fs=Array.from(e.target.files||[]) as File[];fs.slice(0,10).forEach(f=>{const r=new FileReader();r.onload=()=>setPhotos(p=>[...p,String(r.result)]);r.readAsDataURL(f);});};
 const save=async()=>{if(!loanId||!description.trim())return;setBusy(true);setStatus('');try{await collateralAPI.add(Number(loanId),{type,description,value:Number(value||0),photos});setStatus('Dhamana na picha zimehifadhiwa kwenye Loan ID na zitaonekana kwenye agreement/PDF.');setDescription('');setValue('');setPhotos([]);}catch(e:any){setStatus(e?.response?.data?.message||'Dhamana haijahifadhiwa.')}finally{setBusy(false)}};
 return <main className="min-h-screen bg-slate-50 p-4 dark:bg-slate-950"><div className="mx-auto max-w-4xl space-y-5">
  <button onClick={()=>router.back()} className="rounded-xl border bg-white px-4 py-2 text-sm font-bold dark:bg-slate-900">← Rudi nyuma</button>
  <section className="rounded-3xl bg-white p-6 shadow-sm dark:bg-slate-900"><h1 className="text-2xl font-black">📸 Dhamana za Mkopo</h1><p className="mt-1 text-sm text-slate-500">Loan #{loanId||'—'} · {loan?.borrower?.fullName||'—'}</p></section>
  {status&&<div className="rounded-2xl border bg-white p-4 text-sm font-semibold dark:bg-slate-900">{status}</div>}
  <section className="rounded-3xl border bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
   <div className="grid gap-4 sm:grid-cols-2"><label className="text-sm font-bold">Aina<select value={type} onChange={e=>setType(e.target.value)} className="mt-2 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"><option>GARI</option><option>PIKIPIKI</option><option>NYUMBA</option><option>ARDHI</option><option>ELECTRONICS</option><option>OTHER</option></select></label><label className="text-sm font-bold">Thamani (TZS)<input type="number" min="0" value={value} onChange={e=>setValue(e.target.value)} className="mt-2 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/></label></div>
   <label className="mt-4 block text-sm font-bold">Maelezo<textarea value={description} onChange={e=>setDescription(e.target.value)} className="mt-2 min-h-28 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950" placeholder="Namba ya gari, chassis, eneo la mali, hali ya dhamana..." /></label>
   <label className="mt-4 block text-sm font-bold">Picha nyingi za dhamana<input type="file" accept="image/*" multiple onChange={files} className="mt-2 block w-full rounded-xl border p-3"/></label>
   {photos.length>0&&<div className="mt-4 grid grid-cols-2 gap-3 sm:grid-cols-4">{photos.map((p,i)=><div key={i} className="overflow-hidden rounded-xl border"><img src={p} className="h-32 w-full object-cover" alt={'Collateral '+(i+1)}/><button type="button" onClick={()=>setPhotos(x=>x.filter((_,j)=>j!==i))} className="w-full py-1 text-xs font-bold text-red-600">Ondoa</button></div>)}</div>}
   <button disabled={busy||!description.trim()} onClick={save} className="mt-5 w-full rounded-xl bg-blue-600 px-4 py-3 font-black text-white disabled:opacity-50">{busy?'Inahifadhi...':'Hifadhi Dhamana + Picha'}</button>
  </section></div></main>;
}