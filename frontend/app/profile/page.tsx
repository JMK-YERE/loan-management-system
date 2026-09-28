'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { userAPI } from '@/lib/api';

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

  useEffect(() => {
    if (!localStorage.getItem('token')) { router.push('/login'); return; }
    userAPI.me().then((r) => setMe(r.data.data)).catch((e) => setError(e.response?.data?.message || 'Imeshindikana kupakia wasifu'));
  }, [router]);

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
        )}
      </div>
    </div>
  );
}
