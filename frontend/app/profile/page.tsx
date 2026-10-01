'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { userAPI, authAPI } from '@/lib/api';

const ROWS: [string, string][] = [
  ['Barua pepe', 'email'], ['Simu', 'phone'], ['Kitambulisho', 'idType'], ['Namba ya kitambulisho', 'idNumber'],
  ['Tarehe ya kuzaliwa', 'dateOfBirth'], ['Jinsia', 'gender'], ['Hali ya ndoa', 'maritalStatus'], ['Utaifa', 'nationality'],
  ['Anwani', 'address'], ['Mji', 'city'], ['Nchi', 'country'], ['Hali ya ajira', 'employmentStatus'],
  ['Kazi', 'occupation'], ['Mwajiri', 'employer'], ['Kipato cha mwezi', 'monthlyIncome'],
  ['Ndugu wa karibu', 'kinName'], ['Simu ya ndugu', 'kinPhone'], ['Uhusiano', 'kinRelationship'],
];

export default function ProfilePage() {
  const router = useRouter();
  const [me, setMe] = useState<any>(null);
  const [error, setError] = useState('');
  const [pw, setPw] = useState({ currentPassword: '', newPassword: '', confirmPassword: '' });
  const [pwMsg, setPwMsg] = useState('');
  const [pwBusy, setPwBusy] = useState(false);

  useEffect(() => {
    if (!localStorage.getItem('token')) { router.push('/login'); return; }
    userAPI.me().then((r) => setMe(r.data.data)).catch((e) => setError(e.response?.data?.message || 'Imeshindikana kupakia wasifu'));
  }, [router]);

  const changePassword = async (e: React.FormEvent) => {
    e.preventDefault();
    setPwMsg('');
    if (pw.newPassword.length < 8) return setPwMsg('Password mpya iwe na angalau herufi 8.');
    if (pw.newPassword !== pw.confirmPassword) return setPwMsg('Password mpya hazifanani.');
    setPwBusy(true);
    try {
      await authAPI.changePassword({ currentPassword: pw.currentPassword, newPassword: pw.newPassword });
      setPw({ currentPassword: '', newPassword: '', confirmPassword: '' });
      setPwMsg('Password imebadilishwa kikamilifu.');
    } catch (e: any) {
      setPwMsg(e.response?.data?.message || 'Imeshindikana kubadilisha password.');
    } finally {
      setPwBusy(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-950 p-4 sm:p-6">
      <div className="max-w-2xl mx-auto">
        <Link href={me?.role === 'ADMIN' ? '/admin/dashboard' : '/dashboard'} className="text-sm text-blue-600 font-semibold">← Rudi</Link>
        {error && <p className="mt-4 text-red-600 text-sm">{error}</p>}
        {me && (
          <div className="mt-4 bg-white dark:bg-gray-900 rounded-2xl p-6 border border-gray-200 dark:border-gray-800">
            <div className="flex gap-4 items-center mb-6">
              {me.photo ? (
                // eslint-disable-next-line @next/next/no-img-element
                <img src={me.photo} alt="Picha yangu" className="w-28 rounded-lg border border-gray-300 object-cover" style={{ aspectRatio: '35 / 45' }} />
              ) : (
                <div className="w-28 bg-gray-200 rounded-lg flex items-center justify-center text-3xl" style={{ aspectRatio: '35 / 45' }}>👤</div>
              )}
              <div>
                <h1 className="text-2xl font-bold text-gray-900 dark:text-white">{me.fullName}</h1>
                <p className="text-sm text-gray-600 dark:text-gray-400">{me.role}</p>
              </div>
            </div>
            <dl className="grid sm:grid-cols-2 gap-x-6 gap-y-3 text-sm">
              {ROWS.map(([label, key]) => (
                <div key={key}>
                  <dt className="text-gray-500">{label}</dt>
                  <dd className="font-medium text-gray-900 dark:text-white break-words">{me[key] ?? '-'}</dd>
                </div>
              ))}
            </dl>
          </div>
          <div className="mt-5 rounded-2xl border border-blue-100 bg-blue-50 p-5 dark:border-blue-900 dark:bg-blue-950/30">
            <h2 className="font-black text-gray-900 dark:text-white">Usalama wa akaunti</h2>
            <p className="mt-1 text-sm text-gray-600 dark:text-gray-400">Badilisha password ukiwa ndani ya mfumo.</p>
            {pwMsg && <div className="mt-3 rounded-xl border border-blue-200 bg-white p-3 text-sm text-blue-800 dark:border-blue-900 dark:bg-gray-900 dark:text-blue-200">{pwMsg}</div>}
            <form onSubmit={changePassword} className="mt-4 grid gap-3">
              <input required type="password" autoComplete="current-password" placeholder="Password ya sasa" value={pw.currentPassword} onChange={e=>setPw({...pw,currentPassword:e.target.value})} className="rounded-xl border p-3 dark:border-gray-700 dark:bg-gray-950 dark:text-white"/>
              <div className="grid gap-3 sm:grid-cols-2">
                <input required type="password" autoComplete="new-password" placeholder="Password mpya" value={pw.newPassword} onChange={e=>setPw({...pw,newPassword:e.target.value})} className="rounded-xl border p-3 dark:border-gray-700 dark:bg-gray-950 dark:text-white"/>
                <input required type="password" autoComplete="new-password" placeholder="Rudia password mpya" value={pw.confirmPassword} onChange={e=>setPw({...pw,confirmPassword:e.target.value})} className="rounded-xl border p-3 dark:border-gray-700 dark:bg-gray-950 dark:text-white"/>
              </div>
              <button disabled={pwBusy} className="rounded-xl bg-blue-600 px-4 py-3 font-bold text-white disabled:opacity-50">{pwBusy?'Inahifadhi...':'Badilisha Password'}</button>
            </form>
          </div>
        )}
      </div>
    </div>
  );
}
