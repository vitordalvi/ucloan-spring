To-do-list do sistema:


# FEATURES (resumo)
- Lógica do soft-delete de usuários
  - Verificar como vai ficar a lógica de login, porque um usuário desativado não pode logar/fazer alterações
  - O email do usuário que foi deletado poderá ser utilizado novamente?
  - Esse usuário poderia "habilitar" a conta dele em um prazo? Quais regras de negócio ele deveria seguir?
    - O usuário tem outros dependentes vinculados? equipamento?! algo do tipo?!
- Endpoint "forgot-password" de autenticação
  - O usuário foi desabilitado?
- Regras de negócio, funcionalidades de empréstimo

# FIXES
- Arrumar o patch de equipamentos para alterar o modelo de equipamento também


## PENDING

### ApplicationUserController (/users) ->
- (PATCH /me) -> Editar dados do perfil (Primeiro nome, sobrenome, (email?!))
  - Se atualizar o email, ele vai poder ser usado como o username de algum outro usuário?
- (PATCH /me/password) -> Alterar a senha, depois de logado (Acho mais interessante colocar um endpoint específico porque é algo mais crítico)
- (POST /me/photo) -> Endpoint futuro para talvez adicionar uma foto ou algo do tipo
- (DELETE /me) -> Desativar o próprio usuário (vai cair na lógica do soft-delete)

- (GET) -> Retorna todos os usuários, em forma de page
- (GET /id) -> Trás informações do usuário
- (PATCH /id) -> Atualizar os dados de algum usuário
  - Não atualizar a senha do usuário? Acho que por segurança pode colocar para atualizar somente o email e o usuário usa o endpoint recover-password
- (DELETE /id) -> Desativar usuário específico (lógica do soft-delete)
  - Configurar no Security Config a rota

### LoanController (/loans) ->
- (GET /my-loans) -> Retorna page dos empréstimos do usuário (de acordo com o id)
- (GET /id)
  - Para admins: Retorna o empréstimo específico
  - Para usuários: Se o empréstimo for do usuário, retorna ele, se não -> new AccessDeniedException
- (GET /id/history)
  - Para admins: Retorna page do histórico do empréstimo específico
  - Para usuários: Se o empréstimo for do usuário, retorna a ele a página dos históricos do empréstimo, se não -> new AccessDeniedException
- (POST /id/cancel)
  - Para usuários: Pede o cancelamento do empréstimo, o empréstimo vai ter que entrar num status de "CANCEL-PENDING" ou algo do tipo, até o usuário fazer um novo (PATCH /id) onde vai alterar o empréstimo
  - talvez da para seguir a ideia de implementar uma Queue e criar a parte da fila de cancelamento, avaliar certinho
- (POST /id/request-extension)
  - Para usuários: Pede a extensão do tempo de endDate, validar se endDate != null. Pensar na implementação da fila e esses pedidos de extensão de data irem para um QueueController ou algo do tipo, assim como o próprio sistema de fila para fazer a requisição de um equipamento (pelo modelo do equipamento)

- (POST) -> Criar um empréstimo (Admin)
- (PATCH /id) -> Editar um empréstimo (Admin)
- (DELETE /id) -> Deleta um empréstimo (Admin)
  - Se o status não for UNAVAILABLE ou RETURNED, não deve ser possível fazer o retorno do empréstimo
