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
- (GET /me) -> Retorna perfil do próprio usuário (DTO com dados necessários)
- (PATCH /me) -> Editar dados do perfil (Primeiro nome, sobrenome, (email?!))
  - Se atualizar o email, ele vai poder ser usado como o username de algum outro usuário?
- (PATCH /me/password) -> Alterar a senha, depois de logado (Acho mais interessante colocar um endpoint específico porque é algo mais crítico)
- (POST /me) -> Endpoint futuro para talvez adicionar uma foto ou algo do tipo
- (DELETE /me) -> Desativar o próprio usuário (vai cair na lógica do soft-delete)
- (PATCH /admin/id) -> Atualizar os dados de algum usuário
  - Não atualizar a senha do usuário? Acho que por segurança pode colocar para atualizar somente o email e o usuário usa o endpoint recover-password
- (DELETE /admin/id) -> Desativar usuário específico (lógica do soft-delete)
  - Configurar no Security Config a rota