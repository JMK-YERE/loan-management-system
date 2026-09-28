'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { authAPI } from '@/lib/api';
import CameraCapture from '@/components/CameraCapture';

const CODES: [string, string][] = [
  ['+255', 'Tanzania'], ['+254', 'Kenya'], ['+256', 'Uganda'], ['+250', 'Rwanda'], ['+257', 'Burundi'],
  ['+243', 'DR Congo'], ['+260', 'Zambia'], ['+265', 'Malawi'], ['+258', 'Msumbiji'], ['+27', 'Afrika Kusini'],
  ['+234', 'Nigeria'], ['+233', 'Ghana'], ['+251', 'Ethiopia'], ['+20', 'Misri'], ['+971', 'UAE'],
  ['+966', 'Saudi Arabia'], ['+91', 'India'], ['+86', 'China'], ['+44', 'Uingereza'], ['+1', 'USA/Canada'],
  ['+49', 'Ujerumani'], ['+33', 'Ufaransa'], ['other', 'Nyingine...'],
];

type Phone = { code: string; custom: string; num: string };

const buildPhone = (p: Phone) => {
  const raw = p.code === 'other' ? p.custom.replace(/[^\d+]/g, '') : p.code;
  const code = raw.startsWith('+') ? raw : '+' + raw;
  return code + p.num.replace(/\D/g, '').replace(/^0+/, '');
};

const inputCls = 'w-full px-4 py-2.5 rounded-xl border border-gray-300 dark:border-gray-700 bg-white dark:bg-gray-950 text-gray-900 dark:text-white focus:ring-2 focus:ring-blue-500';

function Field({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div>
      <label className="block text-sm font-semibold text-gray-700 dark:text-gray-300 mb-1">{label}</label>
      {children}
    </div>
  );
}

function Section({ title }: { title: string }) {
  return <h2 className="pt-4 text-sm font-bold uppercase tracking-wide text-blue-600 border-b border-gray-200 dark:border-gray-800 pb-1">{title}</h2>;
}

function PhoneField({ value, onChange }: { value: Phone; onChange: (p: Phone) => void }) {
  return (
    <div className="flex gap-2">
      <div className="w-32 shrink-0">
        <select value={value.code} onChange={(e) => onChange({ ...value, code: e.target.value })} className={inputCls}>
          {CODES.map(([c, n]) => <option key={c} value={c}>{c === 'other' ? n : `${n} ${c}`}</option>)}
        </select>
        {value.code === 'other' && (
          <input value={value.custom} onChange={(e) => onChange({ ...value, custom: e.target.value })} placeholder="+code" className={`${inputCls} mt-1`} />
        )}
      </div>
      <input type="tel" required value={value.num} onChange={(e) => onChange({ ...value, num: e.target.value })} className={inputCls} />
    </div>
  );
}

export default function RegisterPage() {
  const router = useRouter();
  const [f, setF] = useState({
    fullName: '', email: '', role: 'BORROWER', dateOfBirth: '', gender: '', maritalStatus: '', nationality: '',
    idType: 'NIDA', idNumber: '', address: '', city: '', country: '', employmentStatus: '', occupation: '',
    employer: '', monthlyIncome: '', kinName: '', kinRelationship: '',
  });
  const [phone, setPhone] = useState<Phone>({ code: '+255', custom: '', num: '' });
  const [kinPhone, setKinPhone] = useState<Phone>({ code: '+255', custom: '', num: '' });
  const [photo, setPhoto] = useState('');
  const [consent, setConsent] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const set = (k: keyof typeof f) => (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
    setF({ ...f, [k]: e.target.value });

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    if (!photo) { setError('Tafadhali piga au pakia picha yako'); return; }
    if (!consent) { setError('Lazima ukubali kuwa taarifa ni sahihi'); return; }
    setLoading(true);
    try {
      const payload = {
        ...f,
        phone: buildPhone(phone),
        kinPhone: buildPhone(kinPhone),
        monthlyIncome: Number(f.monthlyIncome),
        photo,
      };
      console.log('REGISTER PAYLOAD:', payload);
      const res = await authAPI.register(payload);
      setSuccess(res.data.message || 'Usajili umepokelewa');
      window.scrollTo({ top: 0, behavior: 'smooth' });
      setTimeout(() => router.push('/login'), 5000);
    } catch (err: any) {
      const d = err.response?.data;
      let msg = d?.message || 'Usajili umeshindikana. Jaribu tena baada ya sekunde 30.';
      if (d?.data && typeof d.data === 'object') {
        msg += '\n' + Object.values(d.data).join('\n');
      }
      setError(msg);
      window.scrollTo({ top: 0, behavior: 'smooth' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-indigo-100 dark:from-gray-950 dark:via-gray-900 dark:to-indigo-950 flex items-center justify-center p-4 py-12">
      <div className="w-full max-w-2xl">
        <Link href="/" className="flex items-center justify-center gap-2 mb-8">
          <div className="w-12 h-12 rounded-xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center text-white text-2xl shadow-lg">💰</div>
          <span className="text-2xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">JmkLoanApp</span>
        </Link>

        <div className="bg-white dark:bg-gray-900 rounded-2xl p-6 sm:p-8 shadow-xl border border-gray-200 dark:border-gray-800">
          <h1 className="text-2xl font-bold text-gray-900 dark:text-white mb-1">Jisajili</h1>
          <p className="text-gray-600 dark:text-gray-400 mb-4">Jaza taarifa zako kikamilifu. Admin atazikagua kabla ya kukubali.</p>

          {error && <div className="mb-4 p-3 rounded-xl bg-red-50 dark:bg-red-950 border border-red-200 dark:border-red-800 text-red-700 dark:text-red-300 text-sm whitespace-pre-line">⚠️ {error}</div>}
          {success && <div className="mb-4 p-3 rounded-xl bg-green-50 dark:bg-green-950 border border-green-200 dark:border-green-800 text-green-700 dark:text-green-300 text-sm">✅ {success}</div>}

          <form onSubmit={handleRegister} className="space-y-4">
            <Section title="Picha ya pasipoti" />
            <CameraCapture value={photo} onChange={setPhoto} />

            <Section title="Taarifa binafsi" />
            <Field label="Jina Kamili (kama kwenye kitambulisho)">
              <input required value={f.fullName} onChange={set('fullName')} className={inputCls} />
            </Field>
            <div className="grid sm:grid-cols-2 gap-4">
              <Field label="Tarehe ya Kuzaliwa"><input type="date" required value={f.dateOfBirth} onChange={set('dateOfBirth')} className={inputCls} /></Field>
              <Field label="Jinsia">
                <select required value={f.gender} onChange={set('gender')} className={inputCls}>
                  <option value="">Chagua</option><option value="MALE">Me</option><option value="FEMALE">Ke</option>
                </select>
              </Field>
              <Field label="Hali ya Ndoa">
                <select required value={f.maritalStatus} onChange={set('maritalStatus')} className={inputCls}>
                  <option value="">Chagua</option><option value="SINGLE">Sijaoa/Sijaolewa</option><option value="MARRIED">Nimeoa/Nimeolewa</option>
                  <option value="DIVORCED">Nimeachana</option><option value="WIDOWED">Mjane/Mgane</option>
                </select>
              </Field>
              <Field label="Utaifa"><input required value={f.nationality} onChange={set('nationality')} className={inputCls} /></Field>
            </div>

            <Section title="Kitambulisho" />
            <div className="grid sm:grid-cols-2 gap-4">
              <Field label="Aina ya Kitambulisho">
                <select value={f.idType} onChange={set('idType')} className={inputCls}>
                  <option value="NIDA">NIDA</option><option value="PASSPORT">Pasipoti</option><option value="DRIVING_LICENSE">Leseni ya Udereva</option>
                  <option value="VOTER_ID">Kitambulisho cha Mpiga Kura</option><option value="OTHER">Nyingine</option>
                </select>
              </Field>
              <Field label="Namba ya Kitambulisho"><input required minLength={5} maxLength={30} value={f.idNumber} onChange={set('idNumber')} className={inputCls} /></Field>
            </div>

            <Section title="Mawasiliano" />
            <Field label="Barua Pepe"><input type="email" required value={f.email} onChange={set('email')} className={inputCls} /></Field>
            <Field label="Namba ya Simu"><PhoneField value={phone} onChange={setPhone} /></Field>
            <Field label="Anwani (mtaa/kata)"><input required value={f.address} onChange={set('address')} className={inputCls} /></Field>
            <div className="grid sm:grid-cols-2 gap-4">
              <Field label="Mji"><input required value={f.city} onChange={set('city')} className={inputCls} /></Field>
              <Field label="Nchi"><input required value={f.country} onChange={set('country')} className={inputCls} /></Field>
            </div>

            <Section title="Ajira na kipato" />
            <div className="grid sm:grid-cols-2 gap-4">
              <Field label="Hali ya Ajira">
                <select required value={f.employmentStatus} onChange={set('employmentStatus')} className={inputCls}>
                  <option value="">Chagua</option><option value="EMPLOYED">Nimeajiriwa</option><option value="SELF_EMPLOYED">Najiajiri / Biashara</option>
                  <option value="STUDENT">Mwanafunzi</option><option value="RETIRED">Mstaafu</option><option value="UNEMPLOYED">Sina ajira</option>
                </select>
              </Field>
              <Field label="Kazi / Shughuli"><input value={f.occupation} onChange={set('occupation')} className={inputCls} /></Field>
              <Field label="Mwajiri / Jina la Biashara"><input value={f.employer} onChange={set('employer')} className={inputCls} /></Field>
              <Field label="Kipato cha Mwezi"><input type="number" min={0} required value={f.monthlyIncome} onChange={set('monthlyIncome')} className={inputCls} /></Field>
            </div>

            <Section title="Ndugu wa karibu (dharura)" />
            <div className="grid sm:grid-cols-2 gap-4">
              <Field label="Jina Kamili"><input required value={f.kinName} onChange={set('kinName')} className={inputCls} /></Field>
              <Field label="Uhusiano"><input required value={f.kinRelationship} onChange={set('kinRelationship')} className={inputCls} /></Field>
            </div>
            <Field label="Namba ya Simu"><PhoneField value={kinPhone} onChange={setKinPhone} /></Field>

            <Section title="Aina ya akaunti" />
            <Field label="Nataka kujisajili kama">
              <select value={f.role} onChange={set('role')} className={inputCls}>
                <option value="BORROWER">Mkopaji</option><option value="LENDER">Mkopeshaji</option><option value="GUARANTOR">Mdhamini</option>
              </select>
            </Field>

            <label className="flex items-start gap-2 text-sm text-gray-700 dark:text-gray-300 pt-2">
              <input type="checkbox" checked={consent} onChange={(e) => setConsent(e.target.checked)} className="mt-1" />
              Nathibitisha kuwa taarifa na picha nilizotoa ni sahihi, na nakubali zitumike kukagua ombi langu.
            </label>

            <button type="submit" disabled={loading} className="w-full px-6 py-3 bg-gradient-to-r from-blue-600 to-indigo-600 text-white font-bold rounded-xl hover:from-blue-700 hover:to-indigo-700 transition shadow-lg disabled:opacity-50">
              {loading ? 'Inatuma...' : 'Jisajili'}
            </button>
          </form>

          <p className="mt-6 text-center text-sm text-gray-600 dark:text-gray-400">
            Una akaunti? <Link href="/login" className="text-blue-600 font-semibold hover:underline">Ingia</Link>
          </p>
        </div>
      </div>
    </div>
  );
}
