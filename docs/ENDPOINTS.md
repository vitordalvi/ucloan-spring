# EquipmentController (/api/v1/equipments)

- (GET) -> 
  - ADMIN: Retorna EquipmentView (EquipmentAdminResponseDto)
    - EquipmentAdminResponseDto implements EquipmentView
  - USER: Retorna EquipmentView (EquipmentResponseDto)
    - EquipmentResponseDto implements EquipmentView
    - Usuário vai poder pedir um empréstimo do equipamento
      - Regras de negócio? 
        - Exemplo, usuário já possui um empréstimo/Usuário tem um item pendente
      - Criando um empréstimo, usuário automaticamente cria um novo objeto **"Queue"**

- (GET /id) -> 
  - ADMIN: Retorna EquipmentView (EquipmentAdminResponseDto)
  - USER: Retorna EquipmentView (EquipmentResponseDto)
    - Usuário não pode ver todas as informações do equipamento

  @PreAuthorize("hasRole('ADMIN'")
- (PATCH /id) ->
  - Alterar os dados de um equipamento (PatchEquipmentRequestDto)
  - Dados a serem alterados:
    - Descrição;
    - Modelo do Equipamento
      - Validar se o modelo do equipamento existe
  - Estado físico do equipamento (ENUM)
  **SEMPRE QUE UM EQUIPAMENTO TIVER MUDANÇAS, DEVE SER CRIADO UM EQUIPMENT HISTORY**

  @PreAuthorize("hasRole('ADMIN')")
- (POST) ->
  - Criar um equipamento
    - Validar se modelo do equipamento oferecido existe
  - Deve ser criado um EQUIPMENT HISTORY assim que o equipamento for criado

@PreAuthorize("hasRole('ADMIN')")
- (DELETE /id) ->
  - Deleta um equipamento
    - Se o equipamento estiver sendo utilizado em um Empréstimo e não tiver o status "UNAVAILABLE" ou "RETURNED", automaticamente deve ser bloqueado o delete

@PreAuthorize("hasRole('ADMIN')")
- (GET /id/history) ->
  - Retorna EquipmentHistoryAdminResponseDto

@PreAuthorize("hasRole('ADMIN')
- (GET /equipments/history) ->
  - Retorna Page <EquipmentHistoryResponseDto>

  ----------------------------------------------------------------------------------------

# Equipment Models (/api/v1/equipment-models)

- (GET) -> 
  - ADMIN: Retorna EquipmentModelView (EquipmentModelAdminResponseDto)
  - USER: Retorna EquipmentModelView (EquipmentModelResponseDto)
    - Usuários não devem ter acesso à data de criação, data do último update, quem criou e quem deu o update + dados específicos?
    - Usuários possivelmente poderão ver quais equipamentos estão disponíveis para serem usados como empréstimo

- (GET /id) ->
  - ADMIN: Retorna EquipmentModelView (EquipmentModelAdminResponseDto)
  - USER: Retorna EquipmentModelView (EquipmentModelResponseDto)

  @PreAuthorize("hasRole('ADMIN')")
- (PATCH /id) ->
  - Atualiza os campos que forem preenchidos pelo admin

  @PreAuthorize("hasRole('ADMIN')")
- (POST) ->
  - Cria um novo Equipment Model


  





  
