package com.melhorcombustivel

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.ads.AdRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.ads.initialization.InitializationStatus

import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
    lateinit var mAdView : AdView
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        var telaCarro:Boolean = false
        var telaSeuCarro:Boolean = false

        var consumo = DoubleArray(8)
        consumo[0] = 9.5 //consumoEtanolCidade
        consumo[1] = 10.2 //consumoEtanolEstrada

        consumo[2] = 9.6 //consumoEtanolAdtCidade
        consumo[3] = 12.2 //consumoEtanolAdtEstrada

        consumo[4] = 11.2 //consumoGasolinaCidade
        consumo[5] = 14.4 //consumoGasolinaEstrada

        consumo[6] = 11.8 //consumoGasolinaAdtCidade
        consumo[7] = 16.4 //consumoGasolinaAdtEstrada

        //Clicando em Calcular
        botao.setOnClickListener {

            telaCarro = false
            telaSeuCarro = false

            resultado_cidade.visibility = View.VISIBLE
            resultado_estrada.visibility = View.VISIBLE
            bomb.visibility = View.VISIBLE
            bomb_EC.visibility = View.INVISIBLE
            bomb_EA.visibility = View.INVISIBLE
            bomb_GC.visibility = View.INVISIBLE
            bomb_GA.visibility = View.INVISIBLE

            // Lendo os Valores Digitados
            var EC: String = valor_EC.text.toString()
            var EA: String = valor_EA.text.toString()
            var GC: String = valor_GC.text.toString()
            var GA: String = valor_GA.text.toString()

            if (EC.isEmpty() || EA.isEmpty() || GC.isEmpty() || GA.isEmpty()) {
                resultado_cidade.text = "Preencha todos os campos"
            } else {

                // Calculando
                var ECC = calcular(EC, consumo[0])
                var ECE = calcular(EC, consumo[1])

                var EAC = calcular(EA, consumo[2])
                var EAE = calcular(EA, consumo[3])

                var GCC = calcular(GC, consumo[4])
                var GCE = calcular(GC, consumo[5])

                var GAC = calcular(GA, consumo[6])
                var GAE = calcular(GA, consumo[7])


                // Comparando na Cidade
                if (ECC < EAC && ECC < GCC && ECC < GAC) {
                    resultado_cidade.text = "Para cidade: Etanol Comum"
                    bomb.visibility = View.INVISIBLE
                    bomb_EC.visibility = View.VISIBLE
                } else if (EAC < ECC && EAC < GCC && EAC < GAC) {
                    resultado_cidade.text = "Para cidade: Etanol Aditivado"
                    bomb.visibility = View.INVISIBLE
                    bomb_EA.visibility = View.VISIBLE
                } else if (GCC < ECC && GCC < EAC && GCC < GAC) {
                    resultado_cidade.text = "Para cidade: Gasolina Comum"
                    bomb.visibility = View.INVISIBLE
                    bomb_GC.visibility = View.VISIBLE
                } else if (GAC < ECC && GAC < EAC && GAC < GCC) {
                    resultado_cidade.text = "Para cidade: Gasolina Aditivada"
                    bomb.visibility = View.INVISIBLE
                    bomb_GA.visibility = View.VISIBLE
                }

                // Comparando na Estrada
                if (ECE < EAE && ECE < GCE && ECE < GAE) {
                    resultado_estrada.text = "Para estrada: Etanol Comum"
                } else if (EAE < ECE && EAE < GCE && EAE < GAE) {
                    resultado_estrada.text = "Para estrada: Etanol Aditivado"
                } else if (GCE < ECE && GCE < EAE && GCE < GAE) {
                    resultado_estrada.text = "Para estrada: Gasolina Comum"
                } else if (GAE < ECE && GAE < EAE && GAE < GCE) {
                    resultado_estrada.text = "Para estrada: Gasolina Aditivada"
                }
            }
        }

        // Clicando no Icone do Carro
        carro.setOnClickListener {
            telaCarro = true
            esconderTelaInicial()
            mostrarTelaCarro()
            resultado_cidade.text = ""
            resultado_estrada.text = ""
        }

        //Clicando em Escolher carro
        botao2.setOnClickListener {

            telaCarro = false

            var checks = BooleanArray(5)
            checks[0] = check_city.isChecked
            checks[1] = check_civic.isChecked
            checks[2] = check_onix.isChecked
            checks[3] = check_uno.isChecked
            checks[4] = check_sandeiro.isChecked

            var ativos = 0
            var i = 0
            for (i in 0..4){
                if (checks[i] == true) {
                    ativos++
                }
            }
            if (ativos > 1 ) {
                resultado_cidade.visibility = View.VISIBLE
                resultado_cidade.text = "Selecione somente um modelo"
            } else{
                if (checks[0] == true) {
                    consumo = declararConsumoCity()
                } else if (checks[1] == true) {
                    consumo = declararConsumoCivic()
                } else if (checks[2] == true) {
                    consumo = declararConsumoOnix()
                } else if (checks[3] == true) {
                    consumo = declararConsumoUno()
                } else if (checks[4] == true) {
                    consumo = declararConsumoSandeiro()
                }
                esconderTelaCarro()
                mostrarTelaInicial()
                resultado_cidade.visibility = View.INVISIBLE
                resultado_estrada.visibility = View.INVISIBLE
                bomb_EC.visibility = View.INVISIBLE
                bomb_EA.visibility = View.INVISIBLE
                bomb_GC.visibility = View.INVISIBLE
                bomb_GA.visibility = View.INVISIBLE
                valor_EC.setText("")
                valor_EA.setText("")
                valor_GC.setText("")
                valor_GA.setText("")
            }
        }

        //Clicando em Seus Dados
        botao3.setOnClickListener {
           telaSeuCarro = true
            esconderTelaCarro()
            mostrarTelaSeuCarro()
        }

        //Clicando em OK
        botao_ok.setOnClickListener {

            telaSeuCarro = false

            var consumo_ECC: String = consumo_ECC.text.toString()
            var consumo_EAC: String = consumo_EAC.text.toString()
            var consumo_GCC: String = consumo_GCC.text.toString()
            var consumo_GAC: String = consumo_GAC.text.toString()

            var consumo_ECE: String = consumo_ECE.text.toString()
            var consumo_EAE: String = consumo_EAE.text.toString()
            var consumo_GCE: String = consumo_GCE.text.toString()
            var consumo_GAE: String = consumo_GAE.text.toString()

            if (consumo_ECC.isEmpty() || consumo_EAC.isEmpty() || consumo_GCC.isEmpty() || consumo_GAC.isEmpty() || consumo_ECE.isEmpty() || consumo_EAE.isEmpty() || consumo_GCE.isEmpty() || consumo_GAE.isEmpty()) {
                resultado_cidade.visibility = View.VISIBLE
                resultado_cidade.text = "Preencha todos os campos"
            } else {
                consumo[0] = string_double(consumo_ECC)
                consumo[2] = string_double(consumo_EAC)
                consumo[4] = string_double(consumo_GCC)
                consumo[6] = string_double(consumo_GAC)

                consumo[1] = string_double(consumo_ECE)
                consumo[3] = string_double(consumo_EAE)
                consumo[5] = string_double(consumo_GCE)
                consumo[7] = string_double(consumo_GAE)

                resultado_cidade.text = ""
                esconderTelaSeuCarro()
                mostrarTelaInicial()
                bomb_EC.visibility = View.INVISIBLE
                bomb_EA.visibility = View.INVISIBLE
                bomb_GC.visibility = View.INVISIBLE
                bomb_GA.visibility = View.INVISIBLE
                valor_EC.setText("")
                valor_EA.setText("")
                valor_GC.setText("")
                valor_GA.setText("")
            }
        }

        //Clicando em voltar
        voltar.setOnClickListener {
            if(telaSeuCarro == true) {
                esconderTelaSeuCarro()
                esconderTelaInicial()
                mostrarTelaCarro()
                telaSeuCarro = false
            } else if(telaCarro == true){
                esconderTelaCarro()
                esconderTelaSeuCarro()
                mostrarTelaInicial()
                telaCarro = false
            }
        }
    }

    fun string_double (string: String): Double {
        var number: Double = string.toDouble()
        return number
    }

    fun calcular(valor: String, consumo: Double): Double {
        var valor1: Double = valor.toDouble()
        var result: Double = valor1 / consumo
        return result
    }

    fun esconderTelaInicial () :Unit {
        botao.visibility = View.INVISIBLE
        bomb.visibility = View.INVISIBLE
        bomb_EC.visibility = View.INVISIBLE
        bomb_EA.visibility = View.INVISIBLE
        bomb_GC.visibility = View.INVISIBLE
        bomb_GA.visibility = View.INVISIBLE
        Resultado.visibility = View.INVISIBLE
        resultado_estrada.visibility = View.INVISIBLE
        resultado_cidade.visibility = View.INVISIBLE

        EtanolAditivado.visibility = View.INVISIBLE
        EtanolComum.visibility = View.INVISIBLE
        valor_EC.visibility = View.INVISIBLE
        valor_EA.visibility = View.INVISIBLE
        EC_text.visibility = View.INVISIBLE
        EC_text1.visibility = View.INVISIBLE
        EA_text.visibility = View.INVISIBLE
        EA_text1.visibility = View.INVISIBLE

       GasolinaAditivado.visibility = View.INVISIBLE
        GasolinaComum.visibility = View.INVISIBLE
        valor_GC.visibility = View.INVISIBLE
        valor_GA.visibility = View.INVISIBLE
        GC_text.visibility = View.INVISIBLE
        GC_text1.visibility = View.INVISIBLE
        GA_text.visibility = View.INVISIBLE
        GA_text1.visibility = View.INVISIBLE
    }

    fun mostrarTelaInicial () :Unit {
        botao.visibility = View.VISIBLE
        bomb.visibility = View.VISIBLE
        Resultado.visibility = View.VISIBLE
        resultado_estrada.visibility = View.VISIBLE
        resultado_cidade.visibility = View.VISIBLE

        EtanolAditivado.visibility = View.VISIBLE
        EtanolComum.visibility = View.VISIBLE
        valor_EC.visibility = View.VISIBLE
        valor_EA.visibility = View.VISIBLE
        EC_text.visibility = View.VISIBLE
        EC_text1.visibility = View.VISIBLE
        EA_text.visibility = View.VISIBLE
        EA_text1.visibility = View.VISIBLE

        GasolinaAditivado.visibility = View.VISIBLE
        GasolinaComum.visibility = View.VISIBLE
        valor_GC.visibility = View.VISIBLE
        valor_GA.visibility = View.VISIBLE
        GC_text.visibility = View.VISIBLE
        GC_text1.visibility = View.VISIBLE
        GA_text.visibility = View.VISIBLE
        GA_text1.visibility = View.VISIBLE
    }

    fun esconderTelaCarro () :Unit {
        view1.visibility = View.INVISIBLE
        botao2.visibility = View.INVISIBLE
        botao3.visibility = View.INVISIBLE
        check_onix.visibility = View.INVISIBLE
        check_uno.visibility = View.INVISIBLE
        check_city.visibility = View.INVISIBLE
        check_civic.visibility = View.INVISIBLE
        check_sandeiro.visibility = View.INVISIBLE
        voltar.visibility = View.INVISIBLE
    }

    fun mostrarTelaCarro () :Unit {
        view1.visibility = View.VISIBLE
        botao2.visibility = View.VISIBLE
        botao3.visibility = View.VISIBLE
        check_onix.visibility = View.VISIBLE
        check_uno.visibility = View.VISIBLE
        check_city.visibility = View.VISIBLE
        check_civic.visibility = View.VISIBLE
        check_sandeiro.visibility = View.VISIBLE
        voltar.visibility = View.VISIBLE
    }

    fun mostrarTelaSeuCarro () :Unit {
        view1.visibility = View.VISIBLE
        consumo_carro_cidade.visibility = View.VISIBLE
        consumo_carro_estrada.visibility = View.VISIBLE
        consumo_ECC.visibility = View.VISIBLE
        consumo_ECE.visibility = View.VISIBLE
        consumo_EAC.visibility = View.VISIBLE
        consumo_EAE.visibility = View.VISIBLE
        consumo_GCC.visibility = View.VISIBLE
        consumo_GCE.visibility = View.VISIBLE
        consumo_GAC.visibility = View.VISIBLE
        consumo_GAE.visibility = View.VISIBLE
        botao_ok.visibility = View.VISIBLE
        voltar.visibility = View.VISIBLE
    }

    fun esconderTelaSeuCarro () :Unit {
        view1.visibility = View.INVISIBLE
        consumo_carro_cidade.visibility = View.INVISIBLE
        consumo_carro_estrada.visibility = View.INVISIBLE
        consumo_ECC.visibility = View.INVISIBLE
        consumo_ECE.visibility = View.INVISIBLE
        consumo_EAC.visibility = View.INVISIBLE
        consumo_EAE.visibility = View.INVISIBLE
        consumo_GCC.visibility = View.INVISIBLE
        consumo_GCE.visibility = View.INVISIBLE
        consumo_GAC.visibility = View.INVISIBLE
        consumo_GAE.visibility = View.INVISIBLE
        botao_ok.visibility = View.INVISIBLE
        voltar.visibility = View.INVISIBLE
    }

    fun declararConsumoCity(): DoubleArray {

        var consumo = DoubleArray(8)
        consumo[0] = 9.5 //consumoEtanolCidade
        consumo[1] = 10.2 //consumoEtanolEstrada

        consumo[2] = 9.6 //consumoEtanolAdtCidade
        consumo[3] = 12.2 //consumoEtanolAdtEstrada

        consumo[4] = 11.2 //consumoGasolinaCidade
        consumo[5] = 14.4 //consumoGasolinaEstrada

        consumo[6] = 11.5 //consumoGasolinaAdtCidade
        consumo[7] = 16.4 //consumoGasolinaAdtEstrada

        return consumo
    }

    fun declararConsumoCivic() :DoubleArray {
        var consumo = DoubleArray(8)
        consumo[0] = 8.2 //consumoEtanolCidade
        consumo[1] = 8.9 //consumoEtanolEstrada

        consumo[2] = 9.0 //consumoEtanolAdtCidade
        consumo[3] = 10.9 //consumoEtanolAdtEstrada

        consumo[4] = 10.6 //consumoGasolinaCidade
        consumo[5] = 12.9 //consumoGasolinaEstrada

        consumo[6] = 11.0 //consumoGasolinaAdtCidade
        consumo[7] = 14.9 //consumoGasolinaAdtEstrada

        return consumo
    }

    fun declararConsumoOnix() :DoubleArray {
        var consumo = DoubleArray(8)
        consumo[0] = 8.5 //consumoEtanolCidade
        consumo[1] = 10.2 //consumoEtanolEstrada

        consumo[2] = 8.6 //consumoEtanolAdtCidade
        consumo[3] = 12.2 //consumoEtanolAdtEstrada

        consumo[4] = 11.5 //consumoGasolinaCidade
        consumo[5] = 14.9 //consumoGasolinaEstrada

        consumo[6] = 11.8 //consumoGasolinaAdtCidade
        consumo[7] = 16.9 //consumoGasolinaAdtEstrada

        return consumo
    }

    fun declararConsumoUno() :DoubleArray {
        var consumo = DoubleArray(8)
        consumo[0] = 9.2 //consumoEtanolCidade
        consumo[1] = 10.4 //consumoEtanolEstrada

        consumo[2] = 11.2 //consumoEtanolAdtCidade
        consumo[3] = 12.4 //consumoEtanolAdtEstrada

        consumo[4] = 13.1 //consumoGasolinaCidade
        consumo[5] = 15.1 //consumoGasolinaEstrada

        consumo[6] = 15.1 //consumoGasolinaAdtCidade
        consumo[7] = 17.1 //consumoGasolinaAdtEstrada

        return consumo
    }

    fun declararConsumoSandeiro() :DoubleArray {
        var consumo = DoubleArray(8)
        consumo[0] = 9.4 //consumoEtanolCidade
        consumo[1] = 9.9 //consumoEtanolEstrada

        consumo[2] = 9.9 //consumoEtanolAdtCidade
        consumo[3] = 11.9 //consumoEtanolAdtEstrada

        consumo[4] = 12.5 //consumoGasolinaCidade
        consumo[5] = 13.0 //consumoGasolinaEstrada

        consumo[6] = 13.0 //consumoGasolinaAdtCidade
        consumo[7] = 15.0 //consumoGasolinaAdtEstrada

        return consumo
    }
}