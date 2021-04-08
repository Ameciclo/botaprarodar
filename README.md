![Continuous Integration](https://github.com/Ameciclo/botaprarodar/workflows/Continuous%20Integration/badge.svg)

# botaprarodar app

Um sistema autogestionado de compartilhamento de bicicletas como  
alternativa a desigualdades socioespaciais.

[mais informações](https://botaprarodar.ameciclo.org/)

## Gerenciamento do Projeto
Utilizamos o Firebase para gerenciar o app. Temos dois projetos no firebase para Desenvolvimento
e Produção. Para acesso, consulte a equipe de desenvolvimento da aplicação.

## Configuração do projeto
1. Baixe e instale o [Android Studio](https://developer.android.com/studio?gclid=CjwKCAiAmrOBBhA0EiwArn3mfJ_wOSFMNgorYFzGPgb_7jFW8sZL8Rt7MqfhzvMVKILYYaboqkEywhoCXYQQAvD_BwE&gclsrc=aw.ds)
2. Clone este repositório, abra o projeto no Android Studio.
3. Solicite acesso ao console do firebase, baixe e copie o arquivo ***google-services.json*** para o diretório */app* do projeto
4. Clique em Rodar 'app', ou pela *barra de menu* > *rodar* > *rodar 'app'*. Pronto!

## Arquitetura do projeto

O App é um cliente sem estado: todas as operações/mudanças são executadas por meio de chamadas de api pela rede.

Os dados locais são imutáveis, o cliente apenas exibe as informações atualizadas do firebase.  
Os dados locais somente são modificados como resultado de operações realizadas nos endpoints da api.

Objetos de modelo de domínio são utilizados por todo o aplicativo. Estes objetos são simples objetos Kotlin. Eles não devem estar diretamente ligados as camadas de persistência/rede.
A camada de persistência/rede é responsável por traduzir de e para objetos de domínio, conforme necessário. O resto do aplicativo não deve ter que saber sobre os detalhes de implementação de persistência/rede.

Todos os fluxos da aplicação dependem de prévia autenticação de um usuário Administrador gerenciado pelo console do Firebase.  
Uma vez que é feito a validação/autenticação do usuário, este é armazenado em memória e mantido ao longo da execução para futuras consultas na api.

Utilizamos ***MVVM*** como modelo de design de desenvolvimento do projeto.

Estado é armazenado em memória ao longo da execução do aplicativo. Sempre que um recurso é baixado da api, este recurso passa a representar o Estado da aplicação enquanto o aplicativo estiver sendo executado.  
Os dados quando baixados da api são tratados pela camada de persistência/rede por meio de *repositories*, que por sua vez são acessados por *usecases/interactors* que concentram a lógica de negócio da aplicação.

Activities e fragments são apenas para lógica de apresentação. Cada activity e fragment deve ter sua própria *ViewModel* onde a lógica de negócio é tratada.
As *ViewModels* reagem as mudanças de dados nos *usecases/interactors* via coroutines e atualizam a UI por meio de LiveData/DataBinding/ViewBinding.

## Dependências externas

O aplicativo não reinventa a roda. Estes são as APIs/serviços externos que ele depende para funcionar.

- Nosso gerenciamento de dependências é feito com ***Koin***. No arquivo ***app/src/main/java/app/igormatos/botaprarodar/di/Modules.kt*** é configurado todo nosso grafo de dependências e iniciado no arquivo Application do app.
- O Firebase é utilizado como fonte de dados e gerenciamento/autenticação de usuários. É ele, também, que roda nossos testes de UI por meio do TestLab.
- Retrofit para chamadas de rede.
- Kotlin Coroutines para consumo de dados assíncronos.
- Jetpack Navigation para navegação entre telas.
- Glide para consumo/tratamento de imagens.

## Fluxo de Controle de Versão
Por conveniência usuamos a abordagem "Git flow": ***main*** é a branch de release - ela deve sempre estar pronta pra release, e apenas receber merge quando todos os testes e verificações  passarem, garantindo que tudo funciona e está pronta para gerar releases.

O desenvolvimento diário é feito na branch ***development***. Features, bugfixes e outras tarefas são feitas em branches separadas a partir da ***development*** e mergeadas de volta na ***development*** por meio de pull requests.

Quando um pull request é aberto para ***main*** ou ***development*** os testes são executados pelo pipeline de CI.

Mantenha os commits atômicos e auto-explicativos.
Antes de abrir um pull request rode os testes e garanta que todos estejam passando.

## Como testar o projeto
Nossos testes rodam sempre em debug e estão divididos em instrumentados e não instrumentados.

  1. Intrumentados: abra o emulador e execute o comando abaixo
    - ```./gradlew connectedDebugAndroidTest```

  2. Não instrumentados: execute o comando abaixo diretamente no terminal
    - ```./gradlew testDebugUnitTest```

## Integração Contínua
Nosso fluxo de CI é implementado pelo [github actions](https://github.com/Ameciclo/botaprarodar/actions).
Os arquivos de configuração com os jobs ficam localizados no diretório ***/.github*** na raíz do projeto.
Atualmente há dois pipelines de CI:
1. executa a cada pull request aberto pra development ou main, com os jobs
    - Unit Tests
    - Generate APK
    - UI tests no firebase test lab
2. executa a cada push na main, com os jobs:
    - Build
    - Bump Version
    - Deploy

Quando o segundo workflow finaliza com sucesso, o app é enviado por email pra uma lista de usuários
gerenciada pelo Firebase App Distribution.

