# Organizze_Financas_Clone
:dollar: Aplicativo de finanças Organizze App 

## Organizze Clone

Este aplicativo possui funçoes especificas e serve para desenvolver metodos e contole de tela, botão flutuante arrastar para apagar dente
outras funçoes. **Lembrando todo o codigo esta comentado para auxiliar no desenvolvimento e o arquivo Android.docx tambem possui informções
de apoio no desenvolvimento do projeto.**

### Sempre importe a biblioteca e as dependencias no buid do App:

        dependencies {
            implementation fileTree(dir: 'libs', include: ['*.jar'])
            implementation 'com.android.support:appcompat-v7:28.0.0'
            implementation 'com.android.support.constraint:constraint-layout:1.1.3'
            implementation 'com.android.support:design:28.0.0'
            testImplementation 'junit:junit:4.12'
            androidTestImplementation 'com.android.support.test:runner:1.0.2'
            androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
            implementation 'com.google.firebase:firebase-core:16.0.4'
            implementation 'com.google.firebase:firebase-database:16.0.4'
            implementation 'com.google.firebase:firebase-auth:16.0.4'
            implementation 'com.github.clans:fab:1.6.4'
            implementation 'com.prolificinteractive:material-calendarview:1.4.3'
        }



### Algumas telas inlustra melhor a proposta do App:
- #### Tela de inicialização
<img src="/Organizze financas/Prints_tela/Organizze1.png" width="150">
Esta parte foi desenvolvida separadamente e seu codigo esta em ingles pois aprende em um tutorial na internet, esta comentado em portugues.
Seu conteudo é importante pois tem a parte de animação para apresentar as funçoes do aplicativo aos usuarios.

- #### Tela de Inicio, Cadastro e Login

<img src="/Organizze financas/Prints_tela/Organizze2.png" width="150"> <img src="/Organizze financas/Prints_tela/Organizze3.png" width="150"> <img src="/Organizze financas/Prints_tela/Organizze4.png" width="150">

Por tras dessas telas a implementações de validação e verficação de campos, depois tudo e comparado ou enviado para um banco externo
criptografado e em base64.

- #### Menu princial

<img src="/Organizze financas/Prints_tela/Organizze5.png" width="150">

Nesta etapa o usurio consegue atraves de um bottão flutuante criar receitas e despesas, é possivel visualizar saldo atual e apagar notas arrastando para excluir. Tudo isso é possivel aprender nesse projeto.

- #### Despesa

<img src="/Organizze financas/Prints_tela/Organizze6.png" width="150">
Despesa possui as validações de campos e a conexão com banco externo.

- #### Receitas 

<img src="/Organizze financas/Prints_tela/Organizze7.png" width="150">
Receita possui as validações de campos e a conexão com banco externo.

