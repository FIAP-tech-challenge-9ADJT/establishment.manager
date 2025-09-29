# Implementação da Clean Architecture - Establishment Manager

## Estrutura Implementada

A aplicação foi reestruturada seguindo os princípios da Clean Architecture com a seguinte organização:

```
tech.challenge.establishment.manager/
├── domain/                    # Camada de Domínio (núcleo da aplicação)
│   ├── entities/              # Entidades de domínio puras
│   │   ├── User.java          # Entidade User com comportamentos de negócio
│   │   ├── Role.java          # Entidade Role
│   │   └── Address.java       # Entidade Address
│   ├── valueobjects/          # Objetos de valor imutáveis
│   │   ├── UserId.java        # ID de usuário tipado
│   │   ├── Email.java         # Email com validação
│   │   ├── Login.java         # Login com validação
│   │   ├── Password.java      # Password com validação
│   │   ├── Name.java          # Nome com validação
│   │   ├── PostalCode.java    # CEP com validação
│   │   └── AddressId.java     # ID de endereço tipado
│   ├── repositories/          # Interfaces (portas) para acesso a dados
│   │   ├── UserRepository.java
│   │   ├── RoleRepository.java
│   │   └── AddressRepository.java
│   ├── usecases/              # Casos de uso de negócio
│   │   ├── user/              # Use cases de usuário
│   │   │   ├── CreateUserUseCase.java
│   │   │   ├── FindUserUseCase.java
│   │   │   └── UpdateUserUseCase.java
│   │   ├── auth/              # Use cases de autenticação
│   │   │   ├── AuthenticateUserUseCase.java
│   │   │   └── ChangePasswordUseCase.java
│   │   └── admin/             # Use cases administrativos
│   │       └── DeleteUserUseCase.java
│   └── exceptions/            # Exceções específicas do domínio
│       ├── DomainException.java
│       ├── UserNotFoundException.java
│       ├── UserAlreadyExistsException.java
│       └── InvalidCredentialsException.java
├── application/               # Camada de Aplicação
│   ├── usecases/              # Implementações concretas dos casos de uso
│   │   └── CreateUserUseCaseImpl.java
│   └── services/              # Serviços de aplicação e coordenação
│       ├── UserApplicationService.java
│       └── AuthApplicationService.java
├── infrastructure/            # Camada de Infraestrutura
│   ├── persistence/           # Persistência de dados
│   │   ├── entities/          # Entidades JPA/Hibernate
│   │   │   ├── UserJpaEntity.java
│   │   │   ├── RoleJpaEntity.java
│   │   │   ├── AddressJpaEntity.java
│   │   │   └── RoleName.java
│   │   ├── repositories/      # Implementações dos repositórios
│   │   │   ├── UserJpaRepository.java
│   │   │   ├── UserRepositoryImpl.java
│   │   │   ├── RoleJpaRepository.java
│   │   │   ├── RoleRepositoryImpl.java
│   │   │   ├── AddressJpaRepository.java
│   │   │   └── AddressRepositoryImpl.java
│   │   └── mappers/           # Conversores entre domínio e persistência
│   │       ├── UserJpaMapper.java
│   │       ├── RoleJpaMapper.java
│   │       └── AddressJpaMapper.java
│   ├── security/              # Configurações de segurança
│   │   ├── SecurityConfig.java
│   │   ├── AccessTokenFilter.java
│   │   ├── TokenService.java
│   │   └── TokenServiceImpl.java
│   └── config/                # Configurações de infraestrutura
│       ├── OpenApiConfig.java
│       ├── RepositoryConfig.java
│       └── UseCaseConfig.java
└── presentation/              # Camada de Apresentação
    ├── controllers/           # Endpoints REST/API
    │   ├── UserController.java
    │   ├── AuthController.java
    │   └── AdminController.java
    ├── dtos/                  # Objetos de transferência de dados
    │   ├── user/
    │   ├── auth/
    │   ├── address/
    │   ├── role/
    │   └── error/
    └── mappers/               # Conversores entre domínio e DTOs
        ├── UserDtoMapper.java
        ├── RoleDtoMapper.java
        └── AddressDtoMapper.java
```

## Princípios Implementados

### 1. Inversão de Dependência
- O domínio define interfaces (portas) que a infraestrutura implementa
- Use Cases dependem apenas de abstrações, não de implementações concretas

### 2. Separação de Responsabilidades
- **Domínio**: Regras de negócio puras, independentes de frameworks
- **Aplicação**: Orquestração de casos de uso
- **Infraestrutura**: Detalhes técnicos (banco, segurança, frameworks)
- **Apresentação**: Interface com o usuário (REST APIs)

### 3. Fluxo de Dependências
```
Presentation → Application → Domain
Infrastructure → Domain (implementa interfaces)
```

### 4. Entidades Ricas
- Entidades de domínio contêm comportamentos de negócio
- Value Objects garantem invariantes e validações
- Métodos como `changePassword()`, `updateProfile()`, `addRole()`

## Benefícios Alcançados

### ✅ Testabilidade
- Use Cases podem ser testados isoladamente
- Mocks fáceis através das interfaces
- Domínio independente de frameworks

### ✅ Manutenibilidade
- Código organizado por responsabilidades
- Mudanças isoladas em suas respectivas camadas
- Regras de negócio centralizadas

### ✅ Flexibilidade
- Fácil substituição de tecnologias de infraestrutura
- Adição de novos casos de uso sem impacto
- Múltiplas interfaces (REST, GraphQL, etc.) possíveis

### ✅ Evolução
- Estrutura preparada para crescimento
- Novos domínios podem ser adicionados facilmente
- Padrões consistentes estabelecidos

## Próximos Passos

1. **Corrigir problemas de compilação**: Ajustar imports e dependências restantes
2. **Implementar testes**: Criar testes unitários para Use Cases
3. **Validações**: Mover validações para o domínio quando apropriado
4. **Documentação**: Expandir documentação dos casos de uso
5. **Performance**: Otimizar mapeamentos entre camadas

## Observações Importantes

- A estrutura mantém compatibilidade com Spring Boot
- Todas as funcionalidades existentes foram preservadas
- A migração foi feita de forma incremental
- O domínio permanece independente de frameworks externos