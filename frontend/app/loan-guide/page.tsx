'use client';

import Link from 'next/link';
import { ArrowLeft, FileText, Download, Phone, MessageCircle } from 'lucide-react';

export default function LoanGuidePage() {
  return (
    <main className="min-h-screen bg-gray-950 text-white">
      {/* NAVBAR */}
      <nav className="sticky top-0 z-50 bg-gray-950/90 backdrop-blur-lg border-b border-gray-800">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <Link href="/" className="flex items-center gap-2 text-gray-300 hover:text-white transition">
              <ArrowLeft className="w-5 h-5" />
              Rudi Nyumbani
            </Link>
            <Link href="/register" className="px-5 py-2 text-sm font-semibold bg-blue-600 hover:bg-blue-700 text-white rounded-xl transition">
              Jisajili
            </Link>
          </div>
        </div>
      </nav>

      {/* CONTENT */}
      <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="text-center mb-12">
          <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-blue-600 to-indigo-600 flex items-center justify-center mx-auto mb-4">
            <FileText className="w-8 h-8" />
          </div>
          <h1 className="text-3xl md:text-4xl font-bold mb-4">
            Mkataba wa Mkopo
          </h1>
          <p className="text-gray-400 max-w-2xl mx-auto">
            Soma mkataba kamili wa mkopo wa JmkLoanApp kabla ya kujisajili. Hii ni nakala ya mfano.
          </p>
        </div>

        {/* BUTTONS */}
        <div className="flex flex-wrap gap-4 justify-center mb-12">
          <button className="inline-flex items-center gap-2 px-6 py-3 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl transition">
            <Download className="w-5 h-5" />
            Pakua PDF
          </button>
          <Link href="/register" className="inline-flex items-center gap-2 px-6 py-3 border border-gray-700 hover:border-blue-500 text-gray-200 font-bold rounded-xl transition">
            Jisajili Kuanza
          </Link>
        </div>

        {/* AGREEMENT CONTENT */}
        <div className="bg-gray-900 border border-gray-800 rounded-2xl p-8 space-y-8">
          <section>
            <h2 className="text-xl font-bold text-blue-400 mb-4">1. TAARIFA BINAFSI KUHUSU MKOPAJI</h2>
            <div className="space-y-3 text-sm text-gray-300">
              <p><span className="text-gray-500">Jina Kamili:</span> ____________________________</p>
              <p><span className="text-gray-500">NIDA/Kitambulisho:</span> ____________________________</p>
              <p><span className="text-gray-500">Namba ya Simu:</span> ____________________________</p>
              <p><span className="text-gray-500">Anwani:</span> ____________________________</p>
              <p><span className="text-gray-500">Mji/Wilaya:</span> ____________________________</p>
              <p><span className="text-gray-500">Kazi/Biashara:</span> ____________________________</p>
            </div>
          </section>

          <section>
            <h2 className="text-xl font-bold text-blue-400 mb-4">2. TAARIFA ZA AJIRA</h2>
            <div className="space-y-3 text-sm text-gray-300">
              <p><span className="text-gray-500">Mwajiri:</span> ____________________________</p>
              <p><span className="text-gray-500">Namba ya Mwajiri:</span> ____________________________</p>
              <p><span className="text-gray-500">Mwaka wa Kuajiriwa:</span> ____________________________</p>
              <p><span className="text-gray-500">Kipato cha Mwezi:</span> TZS ____________</p>
            </div>
          </section>

          <section>
            <h2 className="text-xl font-bold text-blue-400 mb-4">3. TAARIFA ZA MKOPO</h2>
            <div className="space-y-3 text-sm text-gray-300">
              <p>Mimi ____________________ nimekubali <span className="text-blue-400">JmkLoanApp</span> kunikopeshe kiasi cha <span className="text-blue-400">TZS ____________</span> chenye riba ya <span className="text-blue-400">__%</span>.</p>
              <p>Deni hili nitailipa kwa muda wa miezi <span className="text-blue-400">____</span> kwa awamu <span className="text-blue-400">____</span>.</p>
              <p>Makato ya <span className="text-blue-400">TZS ____________</span> kwa mwezi.</p>
              <p>Jumla ya Marejesho: <span className="text-blue-400">TZS ____________</span></p>
            </div>
          </section>

          <section>
            <h2 className="text-xl font-bold text-blue-400 mb-4">4. DHUMUNI LA MKOPO</h2>
            <p className="text-sm text-gray-300">Mkopo huu nimeuchukua maalum kwa shughuli ya ____________________________ na sitofanyia jambo lingine tofauti na hilo.</p>
          </section>

          <section>
            <h2 className="text-xl font-bold text-blue-400 mb-4">5. DHAMANA YA MKOPO</h2>
            <p className="text-sm text-gray-300">________________________________________________________________________</p>
          </section>

          <section>
            <h2 className="text-xl font-bold text-blue-400 mb-4">6. TAMKO LA MKOPAJI</h2>
            <div className="space-y-3 text-sm text-gray-300">
              <p><strong className="text-white">A.</strong> Nathibitisha kwamba taarifa hizi ni za kweli na nipo tayari kulipa kiasi cha marejesho yote ya mkopo kwa muda husika.</p>
              <p><strong className="text-white">B.</strong> Nikishindwa kulipa deni langu kwa wakati, natamka kwamba taasisi ifuate hatua za kisheria kupata haki yake stahiki.</p>
              <p><strong className="text-white">C.</strong> Nakubali kulipa faini ya <span className="text-blue-400">5%</span> ya kiasi kitakachokuwa kimesalia kwa siku zitakazoongezeka zaidi ya makubaliano ya awali.</p>
              <p><strong className="text-white">D.</strong> Ikitokea JmkLoanApp kutomtendea haki mteja katika malipo ya deni lake, itamlipa mteja faini ya <span className="text-blue-400">1.35%</span> kwa kiwango ambacho haikumtendea haki.</p>
              <p><strong className="text-white">E.</strong> Kiwango cha riba kinaweza kubadilika juu ya mabadiliko hayo.</p>
              <p><strong className="text-white">F.</strong> Pande zote mbili zinakubaliana kwamba kutakuwa na utoaji wa taarifa endapo kutatokea sababu za majanga yaliyo nje ya uwezo wa kibinadamu.</p>
              <p><strong className="text-white">G.</strong> Mkopaji atakayepata janga au shida anapaswa kutoa taarifa kwa mkopeshaji kwa maandishi.</p>
              <p><strong className="text-white">H.</strong> Ikitokea mteja kalipa deni lake kabla ya muda wa mkataba kuisha, atalipa riba pamoja na deni lake lote.</p>
              <p><strong className="text-white">I.</strong> Mkopaji atatakiwa kulipa <span className="text-blue-400">2%</span> kama BIMA ya mkopo.</p>
            </div>
          </section>

          <section>
            <h2 className="text-xl font-bold text-blue-400 mb-4">7. SAHIHI ZA WAHUSIKA</h2>
            <div className="grid md:grid-cols-2 gap-6 text-sm text-gray-300">
              <div>
                <p className="text-white font-bold mb-2">MKOPAJI</p>
                <p>Jina: ____________________</p>
                <p>Sahihi: ____________________</p>
                <p>Tarehe: ____/____/______</p>
              </div>
              <div>
                <p className="text-white font-bold mb-2">MKOPESHAJI</p>
                <p>Jina: ____________________</p>
                <p>Cheo: ____________________</p>
                <p>Sahihi: ____________________</p>
              </div>
              <div>
                <p className="text-white font-bold mb-2">MDHAMINI</p>
                <p>Jina: ____________________</p>
                <p>Sahihi: ____________________</p>
                <p>Tarehe: ____/____/______</p>
              </div>
              <div>
                <p className="text-white font-bold mb-2">SHAHIDI</p>
                <p>Jina: ____________________</p>
                <p>Sahihi: ____________________</p>
                <p>Tarehe: ____/____/______</p>
              </div>
            </div>
          </section>
        </div>

        {/* CTA */}
        <div className="mt-12 text-center">
          <p className="text-gray-400 mb-6">Umesoma mkataba? Anza sasa.</p>
          <Link href="/register" className="inline-flex items-center gap-2 px-8 py-4 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl transition">
            Jisajili Kuanza
          </Link>
        </div>

        {/* CONTACT */}
        <div className="mt-12 pt-8 border-t border-gray-800 text-center text-sm text-gray-400">
          <p className="mb-4">Maswali? Wasiliana nasi:</p>
          <div className="flex flex-wrap justify-center gap-6">
            <a href="tel:0694258683" className="flex items-center gap-2 hover:text-white transition">
              <Phone className="w-4 h-4" />
              0694 258 683
            </a>
            <a href="https://wa.me/255627827053" target="_blank" rel="noopener noreferrer" className="flex items-center gap-2 hover:text-white transition">
              <MessageCircle className="w-4 h-4" />
              0627 827 053
            </a>
          </div>
        </div>
      </div>
    </main>
  );
}
