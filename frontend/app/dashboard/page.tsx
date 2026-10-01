'use client';

import { FormEvent, useEffect, useMemo, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import {
  ArrowRight,
  Calculator,
  CheckCircle2,
  Clock3,
  CreditCard,
  FileText,
  LogOut,
  RefreshCw,
  Search,
  ShieldCheck,
  TrendingUp,
  UserRound,
  XCircle,
} from 'lucide-react';
import { loanAPI, paymentAPI, guarantorAPI, userAPI, loanProductAPI } from '../../lib/api';
import LanguageSwitcher from '@/components/LanguageSwitcher';

const money = (value: any) =>
  new Intl.NumberFormat('sw-TZ', {
    style: 'currency',
    currency: 'TZS',
    maximumFractionDigits: 0,
  }).format(Number(value || 0));

const unwrap = (response: any) => response?.data?.data ?? response?.data ?? [];

const statusLabel: Record<string, string> = {
  PENDING: 'Inasubiri',
  APPROVED: 'Imeidhinishwa',
  REJECTED: 'Imekataliwa',
  DISBURSED: 'Imetolewa',
  PAID: 'Imelipwa',
  DEFAULTED: 'Imechelewa',
};

const statusClass: Record<string, string> = {
  PENDING: 'bg-amber-50 text-amber-700 border-amber-200',
  APPROVED: 'bg-blue-50 text-blue-700 border-blue-200',
  REJECTED: 'bg-red-50 text-red-700 border-red-200',
  DISBURSED: 'bg-indigo-50 text-indigo-700 border-indigo-200',
  PAID: 'bg-emerald-50 text-emerald-700 border-emerald-200',
  DEFAULTED: 'bg-orange-50 text-orange-700 border-orange-200',
};

export default function DashboardPage() {
  const router = useRouter();
  const [user, setUser] = useState<any>(null);
  const [loans, setLoans] = useState<any[]>([]);
  const [selectedLoan, setSelectedLoan] = useState<any>(null);
  const [payments, setPayments] = useState<any[]>([]);
  const [guarantors, setGuarantors] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState(false);
  const [message, setMessage] = useState('');
  const [search, setSearch] = useState('');

  const [amount, setAmount] = useState(2000000);
  const [rate, setRate] = useState(10);
  const [months, setMonths] = useState(3);
  const [processingFee, setProcessingFee] = useState(0);
  const [lawyerRequired, setLawyerRequired] = useState(false);
  const [lawyerFee, setLawyerFee] = useState(0);

  const [borrowerId, setBorrowerId] = useState('');
  const [borrowers, setBorrowers] = useState<any[]>([]);
  const [loanProducts, setLoanProducts] = useState<any[]>([]);
  const [loanProductId, setLoanProductId] = useState('');
  const [purpose, setPurpose] = useState('');

  const [paymentAmount, setPaymentAmount] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('MPESA');

  const totalInterest = useMemo(
    () => (Number(amount) * Number(rate) / 100) * (Number(months) / 12),
    [amount, rate, months]
  );
  const totalRepayment = Number(amount) + totalInterest + Number(processingFee || 0) +
    (lawyerRequired ? Number(lawyerFee || 0) : 0);
  const monthlyInstallment = totalRepayment / Math.max(1, Number(months));

  const loadLoans = async (currentUser: any) => {
    setLoading(true);
    try {
      if (currentUser.role === 'GUARANTOR') {
        const response = await guarantorAPI.mine();
        const data = unwrap(response);
        setGuarantors(Array.isArray(data) ? data : []);
        setLoans([]);
      } else {
        const response = currentUser.role === 'LENDER' ? await loanAPI.byLender() : await loanAPI.byBorrower();
        const data = unwrap(response);
        setLoans(Array.isArray(data) ? data : []);
      }
    } catch (error: any) {
      setMessage(error?.response?.data?.message || 'Imeshindikana kupakia mikopo.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const raw = localStorage.getItem('user');
    const token = localStorage.getItem('token');
    if (!token || !raw) {
      router.push('/login');
      return;
    }
    try {
      const currentUser = JSON.parse(raw);
      if (currentUser.role === 'ADMIN') {
        router.push('/admin/dashboard');
        return;
      }
      if (currentUser.role === 'LENDER') { router.push('/lender'); return; }
      if (currentUser.role === 'BORROWER') { router.push('/borrower'); return; }
      if (currentUser.role === 'GUARANTOR') { localStorage.clear(); router.push('/login'); return; }
      if (currentUser.role === 'BURSER') { router.push('/bursar'); return; }
      setUser(currentUser);
      if (currentUser.role === 'LENDER') {
        userAPI.borrowers().then((r) => setBorrowers(unwrap(r))).catch(() => setBorrowers([]));
        loanProductAPI.active().then((r) => setLoanProducts(unwrap(r))).catch(() => setLoanProducts([]));
      }
      loadLoans(currentUser);
    } catch {
      router.push('/login');
    }
  }, [router]);

  const selectLoan = async (loan: any) => {
    setSelectedLoan(loan);
    setPayments([]);
    try {
      const response = await paymentAPI.byLoan(loan.id);
      const data = unwrap(response);
      setPayments(Array.isArray(data) ? data : []);
    } catch {
      setPayments([]);
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    document.cookie = 'token=; path=/; max-age=0';
    router.push('/login');
  };

  const action = async (fn: () => Promise<any>, success: string) => {
    setBusy(true);
    setMessage('');
    try {
      await fn();
      setMessage(success);
      if (user) await loadLoans(user);
      if (selectedLoan) {
        const refreshed = await loanAPI.get(selectedLoan.id);
        setSelectedLoan(unwrap(refreshed));
      }
    } catch (error: any) {
      setMessage(error?.response?.data?.message || 'Ombi halikukamilika.');
    } finally {
      setBusy(false);
    }
  };

  const createLoan = async (event: FormEvent) => {
    event.preventDefault();
    if (!borrowerId || Number(amount) <= 0) return;
    await action(
      () => loanAPI.create(Number(borrowerId), {
        loanProductId: loanProductId ? Number(loanProductId) : undefined,
        amount: Number(amount),
        interestRate: Number(rate),
        durationMonths: Number(months),
        purpose,
        processingFee: Number(processingFee || 0),
        lawyerRequired,
        lawyerFee: lawyerRequired ? Number(lawyerFee || 0) : 0,
      }),
      'Mkopo umeundwa na umewekwa kusubiri idhini.'
    );
    setPurpose('');
  };

  const submitPayment = async (event: FormEvent) => {
    event.preventDefault();
    if (!selectedLoan || Number(paymentAmount) <= 0) return;
    await action(
      () => paymentAPI.create({
        loanId: selectedLoan.id,
        amount: Number(paymentAmount),
        paymentMethod,
      }),
      'Malipo yamepokelewa na kusubiri uthibitisho.'
    );
    setPaymentAmount('');
    await selectLoan(selectedLoan);
  };

  const filteredLoans = loans.filter((loan) => {
    const text = `${loan.id} ${loan.purpose || ''} ${loan.status || ''}`.toLowerCase();
    return text.includes(search.toLowerCase());
  });

  const stats = {
    total: loans.length,
    pending: loans.filter((l) => l.status === 'PENDING').length,
    approved: loans.filter((l) => ['APPROVED', 'DISBURSED'].includes(l.status)).length,
    paid: loans.filter((l) => l.status === 'PAID').length,
    value: loans.reduce((sum, l) => sum + Number(l.amount || 0), 0),
  };

  if (!user) return null;

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-950">
      <nav className="sticky top-0 z-50 border-b border-slate-200 bg-white/95 px-4 py-3 backdrop-blur dark:border-slate-800 dark:bg-slate-900/95">
        <div className="mx-auto flex max-w-7xl items-center justify-between">
          <Link href="/" className="flex items-center gap-3">
            <div className="flex h-10 w-10 items-center justify-center rounded-xl bg-gradient-to-br from-blue-600 to-indigo-700 text-xl text-white">💰</div>
            <div>
              <div className="font-extrabold text-slate-900 dark:text-white">JmkLoanApp</div>
              <div className="text-[10px] text-slate-500">Loan Management Platform</div>
            </div>
          </Link>
          <div className="flex items-center gap-3">
            <LanguageSwitcher />
            <Link href="/profile" className="hidden items-center gap-2 rounded-xl px-3 py-2 text-sm font-semibold text-slate-600 hover:bg-slate-100 sm:flex dark:text-slate-300 dark:hover:bg-slate-800">
              <UserRound className="h-4 w-4" /> Wasifu
            </Link>
            <button onClick={logout} className="flex items-center gap-2 rounded-xl px-3 py-2 text-sm font-semibold text-red-600 hover:bg-red-50 dark:hover:bg-red-950">
              <LogOut className="h-4 w-4" /> Toka
            </button>
          </div>
        </div>
      </nav>

      <main className="mx-auto max-w-7xl space-y-6 p-4 sm:p-6">
        <section className="rounded-3xl bg-gradient-to-br from-blue-700 via-indigo-700 to-slate-900 p-6 text-white shadow-xl sm:p-8">
          <div className="flex flex-col justify-between gap-5 md:flex-row md:items-center">
            <div>
              <p className="text-sm text-blue-100">Karibu kwenye dashboard</p>
              <h1 className="mt-1 text-3xl font-black">{user.fullName || 'Mtumiaji'}</h1>
              <p className="mt-2 text-sm text-blue-100">Role: {user.role} · Simamia mikopo kwa uwazi na usalama.</p>
            </div>
            <div className="rounded-2xl bg-white/10 p-4 backdrop-blur">
              <div className="text-xs text-blue-100">Thamani ya mikopo</div>
              <div className="mt-1 text-2xl font-black">{money(stats.value)}</div>
            </div>
          </div>
        </section>

        {message && (
          <div className="flex items-center justify-between rounded-2xl border border-blue-200 bg-blue-50 px-4 py-3 text-sm text-blue-800">
            <span>{message}</span>
            <button onClick={() => setMessage('')}><XCircle className="h-4 w-4" /></button>
          </div>
        )}

        {user.role === 'GUARANTOR' && (
          <section className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
            <div className="flex items-center justify-between"><div><h2 className="text-xl font-black text-slate-900 dark:text-white">Maombi ya Udhamini</h2><p className="text-sm text-slate-500">Kagua na ukubali au ukatae maombi yaliyotumwa kwako.</p></div><ShieldCheck className="h-7 w-7 text-blue-600"/></div>
            <div className="mt-5 space-y-3">
              {guarantors.length===0 ? <p className="rounded-xl bg-slate-50 p-4 text-sm text-slate-500 dark:bg-slate-950">Hakuna maombi ya udhamini.</p> : guarantors.map((g:any)=><div key={g.id} className="flex flex-col gap-3 rounded-2xl border p-4 sm:flex-row sm:items-center sm:justify-between dark:border-slate-800">
                <div><b>Loan #{g.loan?.id}</b><p className="text-sm text-slate-500">{money(g.guaranteedAmount)} · {g.relationship || '—'}</p><span className="text-xs font-bold text-blue-600">{g.status}</span></div>
                {g.status==='PENDING' && <div className="flex gap-2"><button disabled={busy} onClick={()=>action(async()=>{await guarantorAPI.approve(g.id);},'Udhamini umeidhinishwa.')} className="rounded-xl bg-emerald-600 px-3 py-2 text-xs font-bold text-white">Kubali</button><button disabled={busy} onClick={()=>action(async()=>{await guarantorAPI.reject(g.id);},'Ombi la udhamini limekataliwa.')} className="rounded-xl bg-red-50 px-3 py-2 text-xs font-bold text-red-600">Kataa</button></div>}
              </div>)}
            </div>
          </section>
        )}

        <section className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {[
            ['Mikopo Yote', stats.total, FileText],
            ['Inasubiri', stats.pending, Clock3],
            ['Imeidhinishwa', stats.approved, TrendingUp],
            ['Imelipwa', stats.paid, CheckCircle2],
          ].map(([label, value, Icon]: any) => (
            <div key={label} className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm dark:border-slate-800 dark:bg-slate-900">
              <Icon className="h-5 w-5 text-blue-600" />
              <div className="mt-4 text-3xl font-black text-slate-900 dark:text-white">{value}</div>
              <div className="text-sm text-slate-500">{label}</div>
            </div>
          ))}
        </section>

        <section className="grid gap-6 lg:grid-cols-2">
          <div className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
            <div className="mb-5 flex items-center gap-3">
              <div className="rounded-xl bg-blue-50 p-2 text-blue-600"><Calculator className="h-5 w-5" /></div>
              <div>
                <h2 className="font-bold text-slate-900 dark:text-white">Kikokotoo cha Mkopo</h2>
                <p className="text-xs text-slate-500">Hesabu riba, ada na marejesho kabla ya kuunda mkopo.</p>
              </div>
            </div>
            <div className="mb-4">
              <label className="text-sm font-semibold text-slate-700 dark:text-slate-300">Aina ya mkopo
                <select value={loanProductId} onChange={(e) => setLoanProductId(e.target.value)} className="mt-1 w-full rounded-xl border p-3 font-normal dark:border-slate-700 dark:bg-slate-950">
                  <option value="">Manual / legacy terms</option>
                  {loanProducts.map((p:any)=><option key={p.id} value={p.id}>{p.name} · {p.loanType} · {p.minDuration}-{p.maxDuration} {String(p.durationUnit).toLowerCase()}</option>)}
                </select>
              </label>
            </div><div className="grid gap-4 sm:grid-cols-2">
              <label className="text-sm font-semibold text-slate-700 dark:text-slate-300">Kiasi
                <input type="number" min="1000" value={amount} onChange={(e) => setAmount(Number(e.target.value))} className="mt-1 w-full rounded-xl border p-3 font-normal dark:border-slate-700 dark:bg-slate-950" />
              </label>
              <label className="text-sm font-semibold text-slate-700 dark:text-slate-300">Riba (% kwa mwaka)
                <input type="number" min="0" step="0.1" value={rate} onChange={(e) => setRate(Number(e.target.value))} className="mt-1 w-full rounded-xl border p-3 font-normal dark:border-slate-700 dark:bg-slate-950" />
              </label>
              <label className="text-sm font-semibold text-slate-700 dark:text-slate-300">Miezi
                <input type="number" min="1" value={months} onChange={(e) => setMonths(Number(e.target.value))} className="mt-1 w-full rounded-xl border p-3 font-normal dark:border-slate-700 dark:bg-slate-950" />
              </label>
              <label className="text-sm font-semibold text-slate-700 dark:text-slate-300">Ada ya usindikaji
                <input type="number" min="0" value={processingFee} onChange={(e) => setProcessingFee(Number(e.target.value))} className="mt-1 w-full rounded-xl border p-3 font-normal dark:border-slate-700 dark:bg-slate-950" />
              </label>
            </div>
            <label className="mt-4 flex items-center gap-3 rounded-xl border p-3 text-sm dark:border-slate-700">
              <input type="checkbox" checked={lawyerRequired} onChange={(e) => setLawyerRequired(e.target.checked)} />
              <span className="font-semibold">Ongeza ada ya wakili</span>
              {lawyerRequired && <input type="number" min="0" value={lawyerFee} onChange={(e) => setLawyerFee(Number(e.target.value))} className="ml-auto w-32 rounded-lg border p-2 dark:border-slate-700 dark:bg-slate-950" />}
            </label>
            <div className="mt-5 grid grid-cols-3 gap-3 rounded-2xl bg-slate-50 p-4 dark:bg-slate-950">
              <div><div className="text-[11px] text-slate-500">Riba</div><div className="font-bold">{money(totalInterest)}</div></div>
              <div><div className="text-[11px] text-slate-500">Jumla</div><div className="font-bold text-blue-600">{money(totalRepayment)}</div></div>
              <div><div className="text-[11px] text-slate-500">Kwa mwezi</div><div className="font-bold">{money(monthlyInstallment)}</div></div>
            </div>
          </div>

          {user.role === 'LENDER' ? (
            <form onSubmit={createLoan} className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
              <div className="mb-5 flex items-center gap-3">
                <div className="rounded-xl bg-emerald-50 p-2 text-emerald-600"><CreditCard className="h-5 w-5" /></div>
                <div>
                  <h2 className="font-bold text-slate-900 dark:text-white">Unda Mkopo</h2>
                  <p className="text-xs text-slate-500">Weka ID ya mkopaji na masharti yaliyokokotolewa.</p>
                </div>
              </div>
              <div className="space-y-4">
                <select required value={borrowerId} onChange={(e) => setBorrowerId(e.target.value)} className="w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950"><option value="">Chagua mkopaji</option>{borrowers.map((b:any)=><option key={b.id} value={b.id}>{b.fullName} · {b.phone}</option>)}</select>
                <input value={purpose} onChange={(e) => setPurpose(e.target.value)} placeholder="Madhumuni ya mkopo" className="w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950" />
                <button disabled={busy} className="flex w-full items-center justify-center gap-2 rounded-xl bg-blue-600 px-4 py-3 font-bold text-white hover:bg-blue-700 disabled:opacity-50">
                  <ShieldCheck className="h-4 w-4" /> {busy ? 'Inatuma...' : 'Unda Mkopo'}
                </button>
                <p className="text-xs text-slate-500">Mkopo mpya utaanza kwenye hali ya <b>PENDING</b>.</p>
              </div>
            </form>
          ) : (
            <div className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm dark:border-slate-800 dark:bg-slate-900">
              <h2 className="font-bold text-slate-900 dark:text-white">Muhtasari wa Mteja</h2>
              <div className="mt-5 space-y-3 text-sm">
                <div className="flex justify-between border-b pb-3"><span className="text-slate-500">Jina</span><b>{user.fullName}</b></div>
                <div className="flex justify-between border-b pb-3"><span className="text-slate-500">Simu</span><b>{user.phone || '—'}</b></div>
                <div className="flex justify-between border-b pb-3"><span className="text-slate-500">Barua pepe</span><b>{user.email || '—'}</b></div>
                <Link href="/profile" className="mt-3 flex items-center justify-between rounded-xl bg-slate-50 p-3 font-semibold text-blue-600 dark:bg-slate-950">
                  Kamilisha wasifu <ArrowRight className="h-4 w-4" />
                </Link>
              </div>
            </div>
          )}
        </section>

        <section className="rounded-3xl border border-slate-200 bg-white shadow-sm dark:border-slate-800 dark:bg-slate-900">
          <div className="flex flex-col gap-3 border-b p-5 sm:flex-row sm:items-center sm:justify-between dark:border-slate-800">
            <div>
              <h2 className="font-bold text-slate-900 dark:text-white">Mikopo Yangu</h2>
              <p className="text-xs text-slate-500">Tafuta, kagua status na fungua historia ya malipo.</p>
            </div>
            <div className="flex gap-2">
              <div className="relative">
                <Search className="absolute left-3 top-3 h-4 w-4 text-slate-400" />
                <input value={search} onChange={(e) => setSearch(e.target.value)} placeholder="Tafuta..." className="rounded-xl border py-2.5 pl-9 pr-3 text-sm dark:border-slate-700 dark:bg-slate-950" />
              </div>
              <button onClick={() => user && loadLoans(user)} className="rounded-xl border p-2.5 hover:bg-slate-50 dark:border-slate-700 dark:hover:bg-slate-800"><RefreshCw className="h-4 w-4" /></button>
            </div>
          </div>

          {loading ? (
            <div className="p-10 text-center text-sm text-slate-500">Inapakia mikopo...</div>
          ) : filteredLoans.length === 0 ? (
            <div className="p-10 text-center text-sm text-slate-500">Hakuna mkopo unaolingana na utafutaji wako.</div>
          ) : (
            <div className="divide-y dark:divide-slate-800">
              {filteredLoans.map((loan) => (
                <div key={loan.id} className="flex flex-col gap-4 p-5 lg:flex-row lg:items-center lg:justify-between">
                  <div className="flex items-start gap-4">
                    <div className="rounded-2xl bg-slate-100 p-3 dark:bg-slate-800"><FileText className="h-5 w-5 text-blue-600" /></div>
                    <div>
                      <div className="flex flex-wrap items-center gap-2">
                        <b className="text-slate-900 dark:text-white">Loan #{loan.id}</b>
                        <span className={`rounded-full border px-2.5 py-1 text-[11px] font-bold ${statusClass[loan.status] || 'bg-slate-50 text-slate-600 border-slate-200'}`}>
                          {statusLabel[loan.status] || loan.status}
                        </span>
                      </div>
                      <p className="mt-1 text-sm text-slate-500">{loan.purpose || 'Hakuna maelezo ya madhumuni'}</p>
                      <p className="mt-1 text-xs text-slate-400">{loan.durationMonths} miezi · {loan.interestRate}% · {money(loan.amount)}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    {user.role === 'LENDER' && loan.status === 'PENDING' && (
                      <>
                        <button disabled={busy} onClick={() => action(() => loanAPI.approve(loan.id), 'Mkopo umeidhinishwa.')} className="rounded-xl bg-emerald-600 px-3 py-2 text-xs font-bold text-white"><CheckCircle2 className="mr-1 inline h-3.5 w-3.5" />Idhinisha</button>
                        <button disabled={busy} onClick={() => action(() => loanAPI.reject(loan.id), 'Mkopo umekataliwa.')} className="rounded-xl bg-red-50 px-3 py-2 text-xs font-bold text-red-600"><XCircle className="mr-1 inline h-3.5 w-3.5" />Kataa</button>
                      </>
                    )}
                    <button onClick={() => selectLoan(loan)} className="rounded-xl border border-slate-200 px-3 py-2 text-xs font-bold text-slate-700 hover:bg-slate-50 dark:border-slate-700 dark:text-slate-200 dark:hover:bg-slate-800">
                      Fungua
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </section>

        {selectedLoan && (
          <section className="rounded-3xl border border-blue-200 bg-white p-6 shadow-lg dark:border-blue-900 dark:bg-slate-900">
            <div className="flex flex-col gap-3 border-b pb-5 sm:flex-row sm:items-center sm:justify-between dark:border-slate-800">
              <div>
                <p className="text-xs font-bold uppercase tracking-wider text-blue-600">Loan #{selectedLoan.id}</p>
                <h2 className="text-xl font-black text-slate-900 dark:text-white">{money(selectedLoan.amount)}</h2>
                <p className="text-sm text-slate-500">Jumla ya kurejesha: {money(selectedLoan.totalRepayment)}</p>
              </div>
              <button onClick={() => setSelectedLoan(null)} className="self-start rounded-xl border px-3 py-2 text-sm dark:border-slate-700">Funga</button>
            </div>

            <div className="mt-5 grid gap-5 lg:grid-cols-3">
              <div>
                <h3 className="font-bold">Muhtasari</h3>
                <div className="mt-3 space-y-2 text-sm">
                  <div className="flex justify-between rounded-xl bg-slate-50 p-3 dark:bg-slate-950"><span className="text-slate-500">Kiasi</span><b>{money(selectedLoan.amount)}</b></div>
                  <div className="flex justify-between rounded-xl bg-slate-50 p-3 dark:bg-slate-950"><span className="text-slate-500">Riba</span><b>{selectedLoan.interestRate}%</b></div>
                  <div className="flex justify-between rounded-xl bg-slate-50 p-3 dark:bg-slate-950"><span className="text-slate-500">Muda</span><b>{selectedLoan.durationMonths} miezi</b></div>
                  <div className="flex justify-between rounded-xl bg-blue-50 p-3 text-blue-700 dark:bg-blue-950"><span>Makadirio kwa mwezi</span><b>{money(Number(selectedLoan.totalRepayment||0)/Math.max(1,Number(selectedLoan.durationMonths||1)))}</b></div>
                </div>
              </div>
              <div>
                <h3 className="font-bold">Ratiba ya Marejesho</h3>
                <div className="mt-3 max-h-64 overflow-auto rounded-xl border dark:border-slate-800">
                  {Array.from({length:Math.max(1,Number(selectedLoan.durationMonths||1))}).map((_,i)=>{
                    const installment=Number(selectedLoan.totalRepayment||0)/Math.max(1,Number(selectedLoan.durationMonths||1));
                    return <div key={i} className="flex justify-between border-b p-3 text-sm last:border-0 dark:border-slate-800"><span>Mwezi {i+1}</span><b>{money(installment)}</b></div>;
                  })}
                </div>
              </div>

              <div>
                <h3 className="font-bold">Historia ya Malipo</h3>
                <div className="mt-3 space-y-2">
                  {payments.length === 0 ? <p className="text-sm text-slate-500">Hakuna malipo bado.</p> : payments.map((p) => (
                    <div key={p.id} className="flex items-center justify-between gap-3 rounded-xl bg-slate-50 p-3 dark:bg-slate-950">
                      <div><b>{money(p.amount)}</b><div className="text-xs text-slate-500">{p.paymentMethod} · {p.status}</div></div>
                      <div className="flex items-center gap-2"><span className="text-xs font-semibold">{p.transactionId || '—'}</span>{user.role==='LENDER' && p.status==='PENDING' && <button disabled={busy} onClick={()=>action(()=>paymentAPI.confirm(p.id,p.transactionId || ''),'Malipo yamethibitishwa.')} className="rounded-lg bg-emerald-600 px-2.5 py-1.5 text-xs font-bold text-white">Thibitisha</button>}</div>
                    </div>
                  ))}
                </div>
              </div>

              {user.role === 'BORROWER' && !['PAID', 'REJECTED'].includes(selectedLoan.status) && (
                <form onSubmit={submitPayment}>
                  <h3 className="font-bold">Fanya Malipo</h3>
                  <div className="mt-3 space-y-3">
                    <input required type="number" min="1" value={paymentAmount} onChange={(e) => setPaymentAmount(e.target.value)} placeholder="Kiasi cha malipo" className="w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950" />
                    <select value={paymentMethod} onChange={(e) => setPaymentMethod(e.target.value)} className="w-full rounded-xl border p-3 dark:border-slate-700 dark:bg-slate-950">
                      {['MPESA','MIXX_BY_YAS','AIRTEL_MONEY','HALOPESA','TANQR','TIPS','TISS','CASH','BANK_TRANSFER'].map((m) => <option key={m}>{m}</option>)}
                    </select>
                    <button disabled={busy} className="w-full rounded-xl bg-blue-600 px-4 py-3 font-bold text-white disabled:opacity-50">Tuma Malipo</button>
                  </div>
                </form>
              )}
            </div>
          </section>
        )}
      </main>
    </div>
  );
}
