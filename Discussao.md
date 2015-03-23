# Introdução #

Discussão sobre como as requisições devem ser tratadas. Supondo que um browser precise conversar com a rede Webp2p, como também conversar com servidores web comuns.

Duas soluções foram propostas:

## Primeira solução ##
  1. Colocar um serviço em determinada porta para que o cliente (browser) possa acessá-lo
  1. Este serviço aceitaria mensagens HTTP
  1. A mensagem recebida teria no seu cabeçalho um identificador, como por exemplo uma tag 

&lt;webp2p&gt;

 que denotaria o uso do serviço.
  1. Ao receber a mensagem, o serviço será capaz de identificá-la e redirecioná-la para um servidor web comum ou utilizar a _overlay_ para descubrir um servidor nela presente.

## Segunda solução ##
  1. Rodar um serviço em uma determinada porta que transfira todas as requisições feitas para a _overlay_.
  1. Configurar um proxy no browser para usar o serviço de redirecionamento.