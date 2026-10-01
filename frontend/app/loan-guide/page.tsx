'use client';

import Link from 'next/link';
import {ArrowLeft,Download,FileSignature} from 'lucide-react';

const Field=({label,wide=false}:{label:string;wide?:boolean})=><span className={'inline-block border-b border-slate-500 min-w-40 '+(wide?'min-w-full':'')}>{' '}</span>;

export default function LoanGuidePage(){
 const printContract=()=>window.print();
 return <main className="min-h-screen bg-slate-100 text-slate-900 dark:bg-slate-950 dark:text-white">
  <nav className="sticky top-0 z-40 border-b bg-white/95 px-4 py-3 backdrop-blur dark:border-slate-800 dark:bg-slate-900/95 print:hidden">
   <div className="mx-auto flex max-w-5xl items-center justify-between gap-3"><Link href="/" className="font-black">💰 JmkLoanApp</Link><div className="flex gap-2"><button onClick={printContract} className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-sm font-bold text-white"><Download className="h-4 w-4"/>Pakua / Print Mkataba</button><Link href="/" className="inline-flex items-center gap-2 rounded-xl border px-4 py-2 text-sm font-bold"><ArrowLeft className="h-4 w-4"/>Rudi</Link></div></div>
  </nav>

  <article className="mx-auto max-w-5xl bg-white p-6 shadow-sm dark:bg-slate-900 sm:p-10 print:max-w-none print:shadow-none print:text-black">
   <header className="border-b-2 border-slate-900 pb-5 text-center">
    <p className="text-sm font-bold uppercase tracking-wider">[JINA LA TAASISI / COMPANY NAME]</p>
    <p className="text-xs">[Anwani ya Ofisi] · [Simu] · [Barua Pepe] · [Namba ya Leseni/Usajili]</p>
    <h1 className="mt-5 text-3xl font-black">MKATABA WA MKOPO</h1>
    <p className="mt-1 text-sm font-semibold">LOAN AGREEMENT · Ref: ____________________ · Toleo: __________</p>
    <p className="mt-4 text-xs leading-5">Mkataba huu unaonyesha masharti ya mkopo yatakayotumika kwa mkopaji husika. Kiasi, riba, ada, adhabu, muda, ratiba na taarifa za wahusika hujazwa kutoka kwenye mfumo wa JmkLoanApp na huhifadhiwa kama snapshot ya mkataba uliokubaliwa.</p>
   </header>

   <section className="mt-7 space-y-5 text-sm leading-6">
    <h2 className="text-lg font-black">1. TAARIFA BINAFSI KUHUSU MKOPAJI</h2>
    <p>Mimi <Field label="Jina kamili"/> Namba ya akaunti/Check No. <Field label=""/> Akaunti ya benki <Field label=""/></p>
    <p>Idara/Kazi <Field label=""/> Kituo ninachopatikana <Field label=""/> Kata <Field label=""/></p>
    <p>Tarafa <Field label=""/> Wilaya <Field label=""/> Mkoa <Field label=""/></p>
    <p>Namba ya NIDA/Kitambulisho <Field label=""/> Simu <Field label=""/> Simu nyingine <Field label=""/></p>
    <p>Anwani ya makazi <Field label="wide"/></p>

    <h2 className="pt-4 text-lg font-black">2. TAARIFA ZA AJIRA / BIASHARA</h2>
    <p>Hali ya ajira <Field label=""/> Mwajiri/Jina la biashara <Field label=""/></p>
    <p>Namba ya mwajiri/PF No. <Field label=""/> Mwaka wa kuanza <Field label=""/> Mwaka wa kustaafu (kama upo) <Field label=""/></p>
    <p>Kipato cha mwezi (TZS) <Field label=""/> Gharama za mwezi (TZS) <Field label=""/> Madeni ya mwezi (TZS) <Field label=""/></p>

    <h2 className="pt-4 text-lg font-black">3. TAARIFA ZA MKOPO</h2>
    <p>Mkopeshaji <b>[JINA LA TAASISI]</b> anakubali kumpa mkopaji mkopo wa <Field label="Kiasi"/> TZS, kwa masharti ya Loan Product <Field label="Product"/>.</p>
    <p>Riba: <Field label=""/> Aina ya riba: <Field label=""/> Ada ya usindikaji: <Field label=""/></p>
    <p>Muda: <Field label=""/> Frequency ya malipo: <Field label=""/> Idadi ya awamu: <Field label=""/></p>
    <p>Jumla ya kurejesha: <Field label=""/> Kiasi cha kila awamu: <Field label=""/></p>
    <p>Malipo ya kwanza: <Field label=""/> Malipo ya mwisho: <Field label=""/> Grace period: <Field label=""/></p>
    <p>Faini/late fee kwa mujibu wa Loan Product: <Field label="wide"/></p>
    <p>Kiasi halisi kitakachotolewa baada ya makato yanayoruhusiwa (Net Disbursement): <Field label=""/></p>

    <h2 className="pt-4 text-lg font-black">4. DHUMUNI LA MKOPO</h2>
    <p>Mkopaji anakiri kuwa mkopo huu umetolewa kwa madhumuni ya:</p>
    <div className="min-h-20 border-b border-slate-400">{' '}</div>

    <h2 className="pt-4 text-lg font-black">5. DHAMANA / COLLATERAL</h2>
    <p>Aina ya dhamana <Field label=""/> Thamani iliyokadiriwa (TZS) <Field label=""/></p>
    <p>Maelezo ya dhamana:</p><div className="min-h-20 border-b border-slate-400">{' '}</div>
    <p>Ushahidi wa dhamana (picha, nyaraka, serial/chassis/registration au maelezo mengine) utaambatanishwa na nakala ya mwisho ya mkataba.</p>

    <h2 className="pt-4 text-lg font-black">6. TAARIFA ZA WATEGEMEZI / NEXT OF KIN</h2>
    <table className="w-full border-collapse text-xs"><thead><tr><th className="border p-2">Na.</th><th className="border p-2">Jina</th><th className="border p-2">Uhusiano</th><th className="border p-2">Simu</th></tr></thead><tbody>{[1,2,3,4,5].map(n=><tr key={n}><td className="border p-3">{n}</td><td className="border">&nbsp;</td><td className="border">&nbsp;</td><td className="border">&nbsp;</td></tr>)}</tbody></table>

    <h2 className="pt-4 text-lg font-black">7. MDHAMINI</h2>
    <p>Ikiwa mkopo huu unahitaji mdhamini, taarifa ya mdhamini itajazwa na lender: Jina <Field label=""/> Simu <Field label=""/> Kitambulisho <Field label=""/></p>
    <p>Uhusiano <Field label=""/> Kiasi anachodhamini (TZS) <Field label=""/></p>
    <p>Mdhamini anaweza kusaini akiwa ofisini mbele ya lender au kwa njia ya remote secure signing link. Picha/kitambulisho/sahihi na timestamp ya tukio vitaunganishwa na Loan ID kwenye mkataba wa mwisho.</p>

    <h2 className="pt-4 text-lg font-black">8. TAMKO LA MKOPAJI</h2>
    <ol className="list-[upper-alpha] space-y-2 pl-6">
     <li>Nathibitisha kuwa taarifa nilizotoa ni za kweli na nipo tayari kulipa marejesho yote kwa mujibu wa mkataba huu.</li>
     <li>Nimepata nafasi ya kusoma kiasi cha mkopo, riba, ada, faini, muda, ratiba na masharti ya marejesho kabla ya kusaini.</li>
     <li>Nikichelewa kulipa, hatua za ukusanyaji na tozo zitatumika kwa mujibu wa masharti ya Loan Product na sheria/kanuni zinazotumika.</li>
     <li>Nitawasiliana na mkopeshaji kwa njia rasmi ikiwa nitapata changamoto inayoweza kuathiri uwezo wangu wa kulipa.</li>
     <li>Malipo ya mapema yatahesabiwa kwa mujibu wa terms zilizowekwa kwenye mkataba huu na taarifa ya payoff itatolewa na mfumo.</li>
     <li>Mabadiliko ya masharti ya baadaye hayatabadilisha kimya kimya snapshot ya mkataba huu; mabadiliko yanayohitaji ridhaa yatafanywa kwa utaratibu unaotambulika.</li>
     <li>Matukio ya dharura yatawasilishwa kwa mkopeshaji na kushughulikiwa kwa mujibu wa sera na sheria zinazotumika.</li>
    </ol>

    <h2 className="pt-4 text-lg font-black">9. MALIPO NA SALIO</h2>
    <p>Mkopaji anaweza kulipa kupitia njia zilizowashwa na taasisi, ikiwemo mobile money au bank transfer. Mfumo utaonyesha kiasi kilicholipwa, salio, overdue, transaction reference na ratiba iliyobaki. Malipo yaliyofanyika kupitia gateway yatahakikiwa kwa reference/idempotency kabla ya kuhesabiwa kama successful.</p>

    <h2 className="pt-4 text-lg font-black">10. SAHIHI NA UTHIBITISHO</h2>
    <div className="grid gap-8 sm:grid-cols-2">
     <div><p className="font-bold">MKOPAJI</p><p>Jina: <Field label=""/></p><p>Sahihi ya kielektroniki: ______________________________</p><p>Tarehe/Muda: ______________________________</p></div>
     <div><p className="font-bold">SHAHIDI WA MKOPAJI</p><p>Jina: <Field label=""/></p><p>NIDA/Kitambulisho: <Field label=""/></p><p>Sahihi: ____________________ Simu: ____________________</p></div>
     <div><p className="font-bold">KWA NIABA YA MKOPESHAJI</p><p>Jina: <Field label=""/></p><p>Cheo: <Field label=""/></p><p>Sahihi: ____________________ Tarehe: ____________________</p></div>
     <div><p className="font-bold">MTHIBITISHAJI / MENEJA</p><p>Jina: <Field label=""/></p><p>Cheo: <Field label=""/></p><p>Sahihi: ____________________ Tarehe: ____________________</p></div>
    </div>

    <div className="mt-8 rounded-2xl border-2 border-slate-800 p-5"><p className="font-black">UTHIBITISHO WA MKOPAJI</p><p className="mt-2">Mimi __________________________________________ nathibitisha kuwa nimesoma, nimeelewa na nimekubali masharti ya mkataba huu pamoja na quotation/repayment schedule iliyohusishwa na Loan ID __________________.</p><div className="mt-5 grid gap-4 sm:grid-cols-2"><div>Sahihi ya kielektroniki: ____________________</div><div>Tarehe: ____________________</div></div></div>

    <p className="pt-5 text-xs leading-5 text-slate-500 print:text-black">Kumbukumbu ya mfumo: nakala ya mwisho ya mkataba itajazwa kwa taarifa halisi za borrower, lender, loan product, fees, repayment schedule, guarantor/collateral evidence na digital signatures. Template hii haifanyi kuwa taasisi ina leseni; taasisi inapaswa kutumia jina lake halali, leseni/maelezo yake na kufanya legal/compliance review kabla ya production.</p>
   </section>

   <div className="mt-8 flex flex-wrap gap-3 print:hidden"><Link href="/login" className="rounded-xl bg-blue-600 px-5 py-3 font-black text-white">Ingia kwenye Mfumo</Link><Link href="/register" className="rounded-xl border px-5 py-3 font-black">Jisajili</Link><Link href="/borrower/apply" className="inline-flex items-center gap-2 rounded-xl border px-5 py-3 font-black"><FileSignature className="h-4 w-4"/>Endelea na Ombi</Link></div>
  </article>
 </main>;
}