# 🖨️ Sistema de Controle de Impressora Elgin
---

## 📘 Sobre o Projeto

Sistema de console em Java que se comunica com uma impressora térmica **Elgin i8/i9** por meio da biblioteca `E1_Impressora01.dll`. O programa tem login, menu com 12 opções e comandos de impressão de texto, QR Code, código de barras, cupom fiscal SAT e cupom de cancelamento, além de abertura de gaveta e sinal sonoro. Desenvolvido para a disciplina de **Programação Orientada a Objetos**.

---

## 🧰 Tecnologias Utilizadas

- **Java 17+**
- **IntelliJ IDEA**
- **JNA 5.15.0** (comunicação com a DLL)
- **DLL E1_Impressora01** (SDK da Elgin)
- **Impressora Elgin i8 ou i9**

---

## 📁 Estrutura do Projeto

```
/src
   Main.java              → Cria os objetos, faz o login e mostra o menu
   Usuario.java           → Guarda o login e a senha e confere se estão corretos
   Impressora.java        → Guarda os dados da conexão e tem um método para cada operação
   ImpressoraDLL.java     → Ligação do Java com a DLL (não alterado)
/Bibliotecas E1 Impressora → DLLs da Elgin (x64 e x86)
/libs Java                → jna-5.15.0.jar
XMLSAT.xml                 → Cupom de exemplo (venda)
CANC_SAT.xml                → Cupom de exemplo (cancelamento)
```

---

## ⚙️ Instalação e Execução

### 1. Baixar o projeto
1. Acesse o repositório no GitHub.
2. Clique em **Code → Download ZIP**.
3. Extraia o arquivo.

### 2. Abrir no IntelliJ IDEA
1. **File → Open** e escolha a pasta extraída.
2. Marque a pasta `src` como **Sources Root** (botão direito → Mark Directory as → Sources Root).
3. Em **File → Project Structure → Libraries**, adicione o `jna-5.15.0.jar` (pasta `libs Java`).
4. Em **Run → Edit Configurations**, confirme que o *Working directory* é a pasta raiz do projeto (onde está a pasta `Bibliotecas E1 Impressora`).
5. Conecte e ligue a impressora na porta USB.
6. Abra `Main.java` e clique em ▶️ **Run**.

### Abrir no VS Code
Abra a pasta raiz do projeto. A configuração em `.vscode/settings.json` adiciona automaticamente os JARs da pasta `libs Java` ao classpath. Se o VS Code já estava aberto, execute **Java: Clean Java Language Server Workspace** e reabra o projeto.

---

## 🔐 Login

- **Usuário:** `lulu`
- **Senha:** `1234`
- Máximo de **3 tentativas**. A cada erro o programa mostra quantas tentativas restam; ao esgotar as 3, o programa é encerrado.

---

## 📋 Funcionalidades do Menu

| Opção | O que faz |
|---|---|
| 1 | Configurar conexão (tipo, modelo, porta, parâmetro) |
| 2 | Abrir conexão com a impressora |
| 3 | Fechar conexão com a impressora |
| 4 | Imprimir texto (posição, estilo e tamanho fixos) e cortar o papel |
| 5 | Imprimir QR Code (tamanho 4, correção 2) e cortar o papel |
| 6 | Imprimir o código de barras CODE 128 fixo `{A012345678912` (altura 100, largura 2, HRI abaixo) e cortar o papel |
| 7 | Imprimir o arquivo `XMLSAT.xml` da raiz do projeto e cortar o papel |
| 8 | Imprimir o arquivo `CANC_SAT.xml` da raiz do projeto e cortar o papel |
| 9 | Abrir gaveta de dinheiro (pino 0, tempos 5 e 10) |
| 10 | Tocar sinal sonoro (bip) |
| 11 | Avançar papel |
| 12 | Ver status da impressora (gaveta, tampa, papel ou geral) |
| 0 | Sair do programa |

Sem conexão, as operações mostram "Sem conexão. Conecte a impressora." e voltam ao menu.

---

## 🧪 Testes Realizados

*(Preencher após testar com a impressora física em sala: login errado 3x, login correto, abrir/fechar conexão, impressão de texto, QR Code, código de barras, XML SAT, XML de cancelamento, bip e abertura de gaveta.)*

---

## 📚 Documentação Adicional

- [Documentação oficial dos comandos da impressora Elgin](https://elgindevelopercommunity.github.io/group___m1.html)

---

## 👥 Autores
*(em ordem alfabética)*

- Ana
- Luiza A
- Luiza B
- Reylla
