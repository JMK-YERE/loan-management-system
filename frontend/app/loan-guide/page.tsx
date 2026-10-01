'use client';

import Link from 'next/link';
import { ArrowLeft, Download, FileCheck2, ShieldCheck, WalletCards } from 'lucide-react';

export default function LoanGuidePage() {
  const printGuide = () => window.print();

  return (
    <main className="min-h-screen bg-slate-50 text-slate-900 dark:bg-slate-950 dark:text-white">
      <nav className="sticky top-0 z-40 border-b bg-white/95 px-4 py-3 backdrop-blur dark:border-slate-800 dark:bg-slate-900/95 print:hidden">
        <div className="mx-auto flex max-w-5xl items-center justify-between gap-3">
          <Link href="/" className="font-black">💰 JmkLoanApp</Link>
          <div className="flex gap-2">
            <button onClick={printGuide} className="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-sm font-bold text-white">
              <Download className="h-4 w-4"/> Pakua / Print PDF
            </button>
            <Link href="/" className="inline-flex items-center gap-2 rounded-xl border px-4 py-2 text-sm font-bold">
              <ArrowLeft className="h-4 w-4"/> Rudi
            </Link>
          </div>
        </div>
      </nav>

      <article className="mx-auto max-w-5xl space-y-6 p-4 sm:p-8">
        <header className="rounded-3xl bg-gradient-to-br from-blue-800 to-slate-950 p-8 text-white print:bg-white print:text-black print:p-0">
          <p className="text-sm font-bold uppercase tracking-wider text-blue-200 print:text-black">JmkLoanApp</p>
          <h1 className="mt-2 text-3xl font-black sm:text-5xl">Mwongozo wa Mkopo na Mkataba</h1>
          <p className="mt-4 max-w-3xl text-sm leading-7 text-blue-100 print:text-black">
            Soma masharti haya kabla ya kuomba mkopo. Mkataba wa mwisho utatengenezwa kwa mkopo wako baada ya lender kuweka offer na masharti halisi.
          </p>
        </header>

        <section className="grid gap-4 md:grid-cols-3 print:grid-cols-3">
          {[
            [FileCheck2,'1. Application','Unaomba kiasi na muda unaohitaji.'],
            [ShieldCheck,'2. Offer & Agreement','Lender anaweka product, riba, ada na ratiba; unasoma na kukubali.'],
            [WalletCards,'3. Sign & Pay','Mdhamini na wahusika husaini, kisha disbursement na marejesho huanza.']
          ].map(([Icon,title,desc]:any)=>
            <div key={title} className="rounded-2xl border bg-white p-5 shadow-sm dark:border-slate-800 dark:bg-slate-900 print:border-slate-300 print:bg-white">
              <Icon className="h-6 w-6 text-blue-600"/>
              <h2 className="mt-3 font-black">{title}</h2>
              <p className="mt-2 text-sm leading-6 text-slate-600 dark:text-slate-400 print:text-black">{desc}</p>
            </div>
          )}
        </section>

        <section className="rounded-3xl border bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900 print:border-slate-300 print:bg-white">
          <h2 className="text-2xl font-black">Kabla hujakubali offer</h2>
          <ul className="mt-4 grid gap-3 text-sm leading-6 text-slate-700 dark:text-slate-300 print:text-black">
            <li>• Angalia kiasi kilichoombwa na kiasi kilichoidhinishwa.</li>
            <li>• Angalia riba, ada ya processing, penalty/late fee na grace period.</li>
            <li>• Angalia jumla utakayorejesha, idadi ya installments na tarehe za malipo.</li>
            <li>• Angalia net disbursement baada ya ada zinazotumika.</li>
            <li>• Soma wajibu wa mkopaji na, kama upo, wajibu wa mdhamini.</li>
            <li>• Usisaini kama taarifa za mkataba si zile ulizoelewa.</li>
          </ul>
        </section>

        <section className="rounded-3xl border bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900 print:border-slate-300 print:bg-white">
          <h2 className="text-2xl font-black">Mtiririko wa JmkLoanApp</h2>
          <div className="mt-5 grid gap-2 sm:grid-cols-2 lg:grid-cols-4">
            {['Register & approval','Loan application','Lender review + offer','Borrower accepts','Credit / conditions','Guarantor consent','Signatures','Disbursement','Repayment','Overdue / collections','Paid / closed'].map((x,i)=>
              <div key={x} className="rounded-xl border p-3 text-sm font-bold dark:border-slate-800 print:border-slate-300"><span className="mr-2 text-blue-600">{i+1}.</span>{x}</div>
            )}
          </div>
        </section>

        <section className="rounded-3xl bg-slate-900 p-6 text-white print:bg-white print:text-black print:border print:border-slate-300">
          <h2 className="text-2xl font-black">Malipo</h2>
          <p className="mt-2 text-sm leading-6 text-slate-300 print:text-black">
            Mfumo umeandaliwa kwa channels za Tanzania kama M-Pesa, Mixx by Yas, Airtel Money, HaloPesa, bank transfer na cash reconciliation. Provider halisi huwashwa kwa credentials na mkataba wa provider kabla ya production.
          </p>
        </section>

        <div className="flex flex-wrap gap-3 print:hidden">
          <Link href="/register" className="rounded-xl bg-blue-600 px-5 py-3 font-black text-white">Jisajili</Link>
          <Link href="/login" className="rounded-xl border px-5 py-3 font-black">Ingia</Link>
          <Link href="/" className="rounded-xl border px-5 py-3 font-black">Rudi Landing Page</Link>
        </div>
      </article>
    </main>
  );
}
