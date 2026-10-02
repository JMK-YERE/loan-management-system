'use client';

import {useEffect,useMemo,useState} from 'react';
import {useRouter} from 'next/navigation';
import Link from 'next/link';
import {Download,RefreshCw,ShieldCheck} from 'lucide-react';
import {loanProductAPI,loanQuoteAPI,generalLoanApplicationAPI,preAgreementAPI} from '@/lib/api';

const money=(v:any)=>new Intl.NumberFormat('sw-TZ',{style:'currency',currency:'TZS',maximumFractionDigits:0}).format(Number(v||0));
const unwrap=(r:any)=>r?.data?.data??r?.data??null;

export default function GeneralLoanApplyPage(){
 const router=useRouter();
 const [products,setProducts]=useState<any[]>([]),[productId,setProductId]=useState('');
 const [amount,setAmount]=useState(''),[duration,setDuration]=useState(''),[purpose,setPurpose]=useState('');
 const [monthlyExpenses,setMonthlyExpenses]=useState(''),[existingMonthlyDebt,setExistingMonthlyDebt]=useState('');
 const [collateralDescription,setCollateralDescription]=useState(''),[collateralValue,setCollateralValue]=useState('');
 const [quote,setQuote]=useState<any>(null),[accepted,setAccepted]=useState(false);
 const [msg,setMsg]=useState(''),[busy,setBusy]=useState(false),[quoteBusy,setQuoteBusy]=useState(false),[previewUrl,setPreviewUrl]=useState('');

 useEffect(()=>{(async()=>{try{const r=await loanProductAPI.active();const ps=unwrap(r);const list=Array.isArray(ps)?ps:[];setProducts(list);if(list[0]){setProductId(String(list[0].id));setDuration(String(list[0].minDuration||1));}}catch(e:any){setMsg(e?.response?.data?.message||'Imeshindikana kupakia loan products.')}})()},[]);
 const product=useMemo(()=>products.find(p=>String(p.id)===String(productId)),[products,productId]);

 useEffect(()=>{
  if(!productId||Number(amount)<=0||Number(duration)<=0){setQuote(null);return}
  const timer=setTimeout(async()=>{setQuoteBusy(true);try{const r=await loanQuoteAPI.quote({productId:Number(productId),amount:Number(amount),duration:Number(duration)});setQuote(unwrap(r));}catch(e:any){setQuote(null);setMsg(e?.response?.data?.message||'Kiasi au muda haupo kwenye product hii.')}finally{setQuoteBusy(false)}},250);
  return()=>clearTimeout(timer);
 },[productId,amount,duration]);

 const submit=async(e:any)=>{
  e.preventDefault();setMsg('');
  if(!quote)return setMsg('Subiri calculator ipate quotation ya product iliyowekwa na ofisi.');
  if(!accepted)return setMsg('Lazima usome mkataba wa mkopo na uthibitishe kabla ya kutuma ombi.');
  setBusy(true);
  try{
   await generalLoanApplicationAPI.submit({amount:Number(amount),duration:Number(duration),purpose:purpose.trim(),productId:Number(productId),monthlyExpenses:Number(monthlyExpenses||0),existingMonthlyDebt:Number(existingMonthlyDebt||0),collateralDescription,collateralValue:Number(collateralValue||0),termsAccepted:true});
   setMsg('Ombi limetumwa. Terms za product zimehifadhiwa kwa quotation ya sasa; lender ataendelea na review.');
   setAccepted(false);setPurpose('');setMonthlyExpenses('');setExistingMonthlyDebt('');setCollateralDescription('');setCollateralValue('');
  }catch(err:any){setMsg(err?.response?.data?.message||'Imeshindikana kutuma ombi.')}finally{setBusy(false)}
 };

 const previewContract=async()=>{
  if(!quote)return setMsg('Weka kiasi na muda kwanza ili preview iwe na figures halisi.');
  try{const r=await preAgreementAPI.pdf({productId:Number(productId),amount:Number(amount),duration:Number(duration),monthlyExpenses:Number(monthlyExpenses||0),existingMonthlyDebt:Number(existingMonthlyDebt||0),collateralDescription,collateralValue:Number(collateralValue||0)},purpose||'Loan application');const url=URL.createObjectURL(r.data);setPreviewUrl(url);}
  catch(e:any){setMsg(e?.response?.data?.message||'Mkataba wa preview haujapatikana.')}
 };

 const downloadContract=async()=>{
  if(!quote)return setMsg('Weka kiasi na muda kwanza ili mkataba wa preview uwe na figures halisi.');
  try{const r=await preAgreementAPI.pdf({productId:Number(productId),amount:Number(amount),duration:Number(duration),monthlyExpenses:Number(monthlyExpenses||0),existingMonthlyDebt:Number(existingMonthlyDebt||0),collateralDescription,collateralValue:Number(collateralValue||0)},purpose||'Loan application');const url=URL.createObjectURL(r.data);const a=document.createElement('a');a.href=url;a.download='jmk-loan-agreement-preview.pdf';a.click();setTimeout(()=>URL.revokeObjectURL(url),5000);}catch(e:any){setMsg(e?.response?.data?.message||'Mkataba wa preview haujapatikana.')}
 };

 useEffect(()=>()=>{if(previewUrl)URL.revokeObjectURL(previewUrl)},[previewUrl]);

 return <main className="min-h-screen bg-slate-50 p-4 dark:bg-slate-950 sm:p-6"><div className="mx-auto max-w-4xl space-y-5">
  <section className="rounded-3xl bg-gradient-to-br from-blue-700 via-indigo-700 to-slate-950 p-6 text-white shadow-xl">
   <button type="button" onClick={()=>router.push('/borrower')} className="mb-4 rounded-xl border border-white/30 px-3 py-2 text-xs font-bold">← Rudi Dashboard</button>
   <div className="flex flex-wrap items-start justify-between gap-3"><div><p className="text-sm text-blue-100">JmkLoanApp · Loan Application</p><h1 className="mt-1 text-3xl font-black">Omba Mkopo</h1><p className="mt-2 max-w-3xl text-sm text-blue-100">Riba, ada, penalty na ratiba havijaandikwa na borrower. Calculator inasoma moja kwa moja masharti ya Loan Product yaliyowekwa na ofisi.</p></div><Link href="/loan-guide" className="rounded-xl border border-white/30 px-3 py-2 text-xs font-bold">📄 Fungua Mkataba</Link></div>
  </section>
  {msg&&<div className="rounded-2xl border border-blue-200 bg-blue-50 p-4 text-sm font-semibold text-blue-800 dark:border-blue-900 dark:bg-blue-950/30 dark:text-blue-200">{msg}</div>}
  <section className="rounded-3xl border bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
   <div className="mb-5 flex items-start justify-between gap-3"><div><h2 className="text-xl font-black">1. Chagua Loan Product</h2><p className="text-sm text-slate-500">Product, riba na ada vinadhibitiwa na taasisi.</p></div><ShieldCheck className="text-blue-600"/></div>
   <select required value={productId} onChange={e=>{setProductId(e.target.value);const p=products.find(x=>String(x.id)===e.target.value);if(p)setDuration(String(p.minDuration||1));}} className="w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950">
    <option value="">Chagua product</option>{products.map(p=><option key={p.id} value={p.id}>{p.name} · {p.loanType} · {p.repaymentFrequency} · {p.interestRate}%</option>)}
   </select>
   {product&&<div className="mt-3 grid gap-3 sm:grid-cols-4">{[['Riba',product.interestRate+'%'],['Interest type',product.interestType],['Ada',money(product.processingFee)],['Other charges',money(product.otherCharges)],['Penalty',money(product.lateFee)]].map(([a,b])=><div key={a} className="rounded-xl bg-slate-50 p-3 dark:bg-slate-950"><div className="text-xs text-slate-500">{a}</div><b>{b}</b></div>)}</div>}
  </section>
  <form onSubmit={submit} className="space-y-5 rounded-3xl border bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
   <div><h2 className="text-xl font-black">2. Kiasi na muda</h2><p className="text-sm text-slate-500">Hapa unaweka principal na muda tu. Huwezi kubadilisha riba/ada za ofisi.</p></div>
   <div className="grid gap-4 sm:grid-cols-2">
    <label className="block text-sm font-bold">Kiasi unachoomba (TZS)<input required min="1" type="number" value={amount} onChange={e=>setAmount(e.target.value)} className="mt-2 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/></label>
    <label className="block text-sm font-bold">Muda ({product?.durationUnit||'unit'})<input required min={product?.minDuration||1} max={product?.maxDuration||undefined} type="number" value={duration} onChange={e=>setDuration(e.target.value)} className="mt-2 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/></label>
   </div>
   <div className="grid gap-4 sm:grid-cols-2"><label className="block text-sm font-bold">Gharama za mwezi (TZS)<input required min="0" type="number" value={monthlyExpenses} onChange={e=>setMonthlyExpenses(e.target.value)} className="mt-2 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/></label><label className="block text-sm font-bold">Madeni ya mwezi (TZS)<input required min="0" type="number" value={existingMonthlyDebt} onChange={e=>setExistingMonthlyDebt(e.target.value)} className="mt-2 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/></label></div>
   <label className="block text-sm font-bold">Dhumuni la mkopo<textarea required maxLength={500} value={purpose} onChange={e=>setPurpose(e.target.value)} className="mt-2 min-h-28 w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"/></label>
   {quote&&<section className="rounded-3xl border-2 border-blue-200 bg-blue-50 p-5 dark:border-blue-900 dark:bg-blue-950/30"><div className="flex items-center justify-between"><div><h3 className="text-lg font-black">Calculator ya taasisi</h3><p className="text-xs text-slate-500">Hesabu hii imetoka server kwa Loan Product #{quote.productId}.</p></div>{quoteBusy&&<RefreshCw className="h-5 w-5 animate-spin text-blue-600"/>}</div><div className="mt-4 grid gap-3 sm:grid-cols-2 lg:grid-cols-4">{[['Principal',money(quote.principal)],['Riba',money(quote.interest)],['Ada',money(quote.processingFee)],['Other charges',money(quote.otherCharges)],['Jumla ya kurejesha',money(quote.totalRepayment)],['Idadi ya malipo',quote.installmentCount],['Kila malipo',money(quote.installmentAmount)],['Frequency',quote.repaymentFrequency],['Penalty',money(quote.lateFee)]].map(([a,b])=><div key={a} className="rounded-xl bg-white p-4 dark:bg-slate-900"><div className="text-xs text-slate-500">{a}</div><b>{b}</b></div>)}</div><div className="mt-3 text-xs text-slate-500">Mkataba wa preview unaweza kupakuliwa hapa chini; figures hizi si terms zinazoweza kubadilishwa na borrower.</div></section>}
   <div className="flex flex-wrap gap-2"><button type="button" onClick={previewContract} disabled={!quote} className="rounded-xl bg-blue-600 px-4 py-2 text-sm font-black text-white disabled:opacity-50">👁 Preview Mkataba</button><button type="button" onClick={downloadContract} className="rounded-xl border px-4 py-2 text-sm font-black"><Download className="mr-1 inline h-4 w-4"/>Pakua Mkataba wa Preview</button><Link href="/loan-guide" className="rounded-xl border px-4 py-2 text-sm font-black">Soma Mkataba wa Mfumo</Link></div>
   <div className="rounded-2xl border bg-slate-50 p-4 dark:border-slate-800 dark:bg-slate-950"><label className="flex items-start gap-3 text-sm"><input type="checkbox" checked={accepted} onChange={e=>setAccepted(e.target.checked)} className="mt-1 h-5 w-5"/><span><b>Nimesoma mkataba.</b> Nimeona principal, riba, ada, jumla ya marejesho, frequency na penalty kwenye quotation hii na ninaelewa kuwa lender bado atafanya credit review kabla ya final approval.</span></label></div>
   <button disabled={busy||!quote||!accepted} className="w-full rounded-xl bg-blue-600 px-4 py-3 font-black text-white disabled:opacity-50">{busy?'Inatuma...':'Tuma ombi la mkopo'}</button>
   <Link href="/borrower" className="block text-center text-sm font-bold text-blue-600">Rudi Dashboard</Link>
  </form>
 </div>
 {previewUrl&&<div className="fixed inset-0 z-50 flex items-center justify-center bg-black/70 p-3 sm:p-6"><div className="flex h-[95vh] w-full max-w-5xl flex-col overflow-hidden rounded-2xl bg-white shadow-2xl dark:bg-slate-900"><div className="flex items-center justify-between border-b p-4"><div><h3 className="font-black">Preview ya Mkataba wa Mkopo</h3><p className="text-xs text-slate-500">Terms zimehesabiwa kutoka Loan Product ya ofisi.</p></div><button type="button" onClick={()=>{URL.revokeObjectURL(previewUrl);setPreviewUrl('')}} className="rounded-xl border px-4 py-2 text-sm font-black">✕ Funga</button></div><iframe title="Preview ya Mkataba wa Mkopo" src={previewUrl} className="min-h-0 flex-1 w-full" /></div></div>}</main>;
}