# Caça Cristais AR

Jogo Android de realidade aumentada baseado no template do professor Jailton Coelho. Escolha um cristal, aponte a câmera para o ambiente, toque em **Posicionar** e depois em **Coletar**. Cada coleta vale 10 pontos; um novo cristal é carregado para continuar. O botão de seleção permite escolher entre três cristais. Há uma trilha ambiente em repetição durante a tela de câmera.

## Como executar

1. Abra o projeto no Android Studio e aguarde a sincronização do Gradle.
2. Use um celular Android compatível com ARCore e com os Serviços do Google Play para RA instalados ou atualizados.
3. Habilite depuração USB, conecte o aparelho e execute o módulo `app`.
4. Conceda a permissão de câmera, encontre um ambiente iluminado e posicione e colete um cristal. Na aba Cristais, selecione os outros modelos.

A pontuação é mantida durante uma mudança de configuração da tela, mas não é salva entre sessões. A coleta é acionada pelo botão, não por toque direto no modelo. O funcionamento da câmera, rastreamento, áudio e renderização precisa ser conferido em um dispositivo real.

## Modelos e áudio

Os modelos `cristal_azul.glb`, `cristal_verde.glb` e `cristal_roxo.glb` são geometria original gerada especificamente para este projeto, disponibilizada sob [CC0 1.0](https://creativecommons.org/publicdomain/zero/1.0/). O som `ambiente.wav` é uma composição sintética original criada para este projeto e também disponibilizada sob CC0 1.0. Os modelos herdados do template não são oferecidos na seleção do jogo.

O código original do template mantém sua licença MIT; veja `LICENSE`.
