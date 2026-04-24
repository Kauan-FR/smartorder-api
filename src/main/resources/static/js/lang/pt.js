/**
 * Portuguese (BR) translations for SmartOrder admin panel.
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */

I18n.register('pt', {

    // ==================== Sidebar ====================
    sidebar: {
        brand: 'SmartOrder',
        sectionMain: 'Principal',
        dashboard: 'Painel',
        categories: 'Categorias',
        products: 'Produtos',
        users: 'Usuários',
        addresses: 'Endereços',
        sectionSales: 'Vendas',
        orders: 'Pedidos',
        orderItems: 'Itens do pedido'
    },

    // ==================== Topbar ====================
    topbar: {
        searchPlaceholder: 'Pesquisar...',
        language: 'Idioma',
        theme: 'Alternar tema',
        notifications: 'Notificações',
        notifClearAll: 'Limpar tudo',
        notifEmpty: 'Nenhuma notificação'
    },

    // ==================== Profile Popup ====================
    popup: {
        profile: 'Perfil',
        settings: 'Configurações',
        help: 'Ajuda',
        signOut: 'Sair'
    },

    // ==================== Help Submenu ====================
    helpMenu: {
        helpCenter: 'Central de ajuda',
        releaseNotes: 'Notas da versão',
        termsAndPolicies: 'Termos e políticas',
        reportBug: 'Reportar um bug'
    },

    // ==================== Help Center Modal ====================
    helpCenter: {
        title: 'Central de Ajuda',
        description: 'Encontre respostas para as perguntas mais frequentes sobre o painel administrativo do SmartOrder.',
        faq1q: 'Como adicionar um novo produto?',
        faq1a: 'Navegue até Produtos na barra lateral, clique no botão "Novo produto" no canto superior direito, preencha os detalhes do produto (nome, descrição, preço, estoque, categoria e URL da imagem), e clique em "Salvar". Você também pode definir uma porcentagem de desconto e marcar o produto como destaque.',
        faq2q: 'Como alterar minha senha?',
        faq2a: 'Acesse Perfil pelo menu popup na parte inferior da barra lateral. Na seção de segurança, clique em "Alterar senha", insira sua senha atual seguida da nova senha e confirmação, e salve.',
        faq3q: 'O que significam os status dos pedidos?',
        faq3aTitle: 'Status dos pedidos:',
        faq3aPending: 'Pendente — Pedido foi realizado mas ainda não confirmado pela loja.',
        faq3aConfirmed: 'Confirmado — Loja aceitou o pedido e está preparando.',
        faq3aShipped: 'Enviado — Pedido foi despachado para entrega.',
        faq3aDelivered: 'Entregue — Pedido entregue com sucesso ao cliente.',
        faq3aCancelled: 'Cancelado — Pedido foi cancelado pela loja ou cliente.',
        faq4q: 'Como criar uma nova categoria?',
        faq4a: 'Navegue até Categorias na barra lateral, clique em "Nova categoria", insira o nome da categoria e uma descrição opcional, e clique em "Salvar". Os produtos podem então ser atribuídos a esta categoria.',
        faq5q: 'Posso excluir uma conta de usuário?',
        faq5a: 'Sim. Acesse Usuários, encontre o usuário na tabela e clique no ícone de exclusão. Um diálogo de confirmação aparecerá. Note que você não pode excluir sua própria conta pelo painel admin por questões de segurança.',
        faq6q: 'Como configurar uma promoção de produto?',
        faq6a: 'Edite o produto e preencha o campo Desconto % (ex: 30 para 30% de desconto). Opcionalmente defina uma data de expiração da promoção. O preço final será calculado automaticamente. Você também pode definir o Estoque inicial para mostrar uma barra de progresso de quanto foi vendido.',
        faq7q: 'Como funciona a barra de pesquisa?',
        faq7a: 'A barra de pesquisa no topo de cada página CRUD filtra a tabela em tempo real pelo nome. Basta digitar e os resultados serão atualizados instantaneamente — sem necessidade de pressionar Enter.'
    },

    // ==================== Release Notes Modal ====================
    releaseNotes: {
        title: 'Notas da Versão',
        description: 'Acompanhe as novidades e melhorias do SmartOrder.',
        latest: 'Mais recente',
        v120: 'Chat em tempo real via WebSocket (STOMP + SockJS)',
        v120b: 'Avaliações de produtos com estrelas e sistema de curtidas',
        v120c: 'Sistema de favoritos para clientes',
        v120d: 'Carrinho de compras persistido no banco de dados',
        v120e: 'Respostas rápidas para vendedores no chat',
        v110: 'Painel admin com métricas e barras de status',
        v110b: 'CRUD completo para todas as entidades (categorias, produtos, usuários, endereços, pedidos, itens)',
        v110c: 'Tema escuro/claro com detecção de preferência do sistema',
        v110d: 'Barra lateral colapsável com persistência de estado',
        v100: 'Lançamento inicial com backend Spring Boot 4.0',
        v100b: 'Autenticação JWT com controle de acesso por função',
        v100c: 'Documentação Swagger/OpenAPI',
        v100d: 'Migrações de banco de dados Flyway (24 migrações)',
        january: 'Janeiro 2026',
        february: 'Fevereiro 2026',
        march: 'Março 2026',
        april: 'Abril 2026',
        may: 'Maio 2026',
        june: 'Junho 2026',
        july: 'Julho 2026',
        august: 'Agosto 2026',
        september: 'Setembro 2026',
        october: 'Outubro 2026',
        november: 'Novembro 2026',
        december: 'Dezembro 2026'
    },

    // ==================== Terms & Policies Modal ====================
    terms: {
        title: 'Termos e Políticas',
        tabTerms: 'Termos de Uso',
        tabPrivacy: 'Política de Privacidade',
        tabCookie: 'Política de Cookies',
        lastUpdated: 'Última atualização: Março 2026',
        // Termos de Uso
        t1title: '1. Aceitação dos Termos',
        t1text: 'Ao acessar e utilizar a plataforma SmartOrder, você concorda em ficar vinculado a estes Termos de Uso. Se você não concordar com qualquer parte destes termos, não deve utilizar a plataforma.',
        t2title: '2. Uso da Plataforma',
        t2text: 'O SmartOrder fornece ferramentas de gerenciamento de e-commerce para administradores e clientes autorizados. Você concorda em utilizar a plataforma apenas para fins legais e de acordo com estes termos. Você é responsável por manter a confidencialidade de suas credenciais de conta.',
        t3title: '3. Contas de Usuário',
        t3text: 'Você deve fornecer informações precisas e completas ao criar uma conta. Cada usuário é responsável por todas as atividades que ocorrem em sua conta. Administradores possuem privilégios elevados e devem exercê-los de forma responsável.',
        t4title: '4. Propriedade Intelectual',
        t4text: 'Todo o conteúdo, designs e funcionalidades do SmartOrder são propriedade do dono da plataforma. Você não pode reproduzir, distribuir ou criar trabalhos derivados sem permissão explícita por escrito.',
        t5title: '5. Limitação de Responsabilidade',
        t5text: 'O SmartOrder é fornecido "como está" sem garantias de qualquer tipo. Não seremos responsáveis por quaisquer danos indiretos, incidentais ou consequenciais decorrentes do uso da plataforma.',
        // Política de Privacidade
        p1title: '1. Informações que Coletamos',
        p1text: 'Coletamos informações que você fornece diretamente: nome, endereço de e-mail, número de telefone e endereços de entrega. Também coletamos dados de uso como timestamps de login e histórico de pedidos para melhorar a experiência na plataforma.',
        p2title: '2. Como Usamos Suas Informações',
        p2text: 'Suas informações são usadas para processar pedidos, gerenciar sua conta, comunicar atualizações importantes e melhorar nossos serviços. Não vendemos nem compartilhamos seus dados pessoais com terceiros para fins de marketing.',
        p3title: '3. Segurança dos Dados',
        p3text: 'Implementamos medidas de segurança padrão da indústria incluindo autenticação JWT, criptografia de senha (BCrypt) e criptografia HTTPS para proteger seus dados. No entanto, nenhum método de transmissão pela internet é 100% seguro.',
        p4title: '4. Seus Direitos (LGPD)',
        p4text: 'De acordo com a Lei Geral de Proteção de Dados (LGPD), você tem o direito de acessar, corrigir, excluir ou exportar seus dados pessoais. Você pode solicitar a exclusão da conta através das configurações do perfil ou entrando em contato com o suporte.',
        // Política de Cookies
        c1title: '1. O que São Cookies',
        c1text: 'Cookies são pequenos arquivos de texto armazenados no seu dispositivo quando você visita nossa plataforma. Nós os utilizamos para lembrar suas preferências e melhorar sua experiência.',
        c2title: '2. Cookies que Utilizamos',
        c2textEssential: 'Cookies essenciais: Necessários para autenticação (armazenamento do token JWT) e funcionalidade básica da plataforma. Estes não podem ser desabilitados.',
        c2textPreference: 'Cookies de preferência: Armazenam sua preferência de tema (modo escuro/claro) e estado da barra lateral. Melhoram sua experiência mas não são estritamente necessários.',
        c3title: '3. Gerenciando Cookies',
        c3text: 'Você pode gerenciar cookies através das configurações do seu navegador. Note que desabilitar cookies essenciais impedirá você de fazer login na plataforma.'
    },

    // ==================== Report a Bug Modal ====================
    reportBug: {
        title: 'Reportar um Bug',
        description: 'Encontrou algo errado? Nos avise e corrigiremos o mais rápido possível.',
        labelTitle: 'Título',
        placeholderTitle: 'Breve descrição do problema',
        labelCategory: 'Categoria',
        categoryDefault: 'Selecione uma categoria',
        categoryUi: 'Interface / Problema visual',
        categoryFunc: 'Funcionalidade não funciona',
        categoryPerf: 'Desempenho / Carregamento lento',
        categoryData: 'Dados não salvam / não exibem',
        categoryAuth: 'Login / Problema de autenticação',
        categoryOther: 'Outro',
        labelDescription: 'Descrição',
        placeholderDescription: 'Descreva o que aconteceu, o que você esperava e os passos para reproduzir...',
        btnCancel: 'Cancelar',
        btnSubmit: 'Enviar relatório',
        successMessage: 'Relatório de bug enviado com sucesso! Nossa equipe irá analisá-lo.'
    },

    // ====================== Help JS ======================
    helpJs: {
        msgSubmitted: 'Relatório de erro enviado com sucesso! Nossa equipe irá analisá-lo.'
    },

    // ==================== Dashboard ====================
    dashboard: {
        title: 'Painel',
        welcome: 'Bem-vindo de volta',
        totalOrders: 'Total de pedidos',
        revenue: 'Receita',
        users: 'Usuários',
        products: 'Produtos',
        recentOrders: 'Pedidos recentes',
        ordersByStatus: 'Pedidos por status',
        quickActions: 'Ações rápidas',
        newProduct: 'Novo produto',
        newCategory: 'Nova categoria',
        addAdminUser: 'Adicionar administrador',
        loadingOrders: 'Carregando pedidos...'
    },

    // ====================== Dashboard JS ======================
    dashboardJs: {
        welcome: 'Bem-vindo de volta',
        failedOrders: 'Falha ao carregar pedidos',
        noOrders: 'Nenhum pedido ainda',
        failedStatus: 'Falha ao carregar'
    },

    // ==================== Categories Page ====================
    categories: {
        title: 'Categorias',
        subtitle: 'Gerenciar categorias de produtos',
        newCategory: 'Nova categoria',
        thName: 'Nome',
        thDescription: 'Descrição',
        thActions: 'Ações',
        formName: 'Nome',
        formDescription: 'Descrição',
        placeholderDescription: 'Descrição desta categoria...',
        placeholderEG: 'ex: Eletrônicos',
        createTitle: 'Nova categoria',
        searchCategories: 'Pesquisar categorias...',
        loadingCategories: 'Carregando categorias...',
        deleteCategory: 'Excluir categoria',
        deleteCategoryText: 'Tem certeza de que deseja excluir esta categoria? Isso não excluirá os produtos atribuídos a ela, mas eles ficarão sem categoria.'
    },

    // ==================== Categories JS ====================
    categoriesJs: {
        failedCategories: 'Falha ao carregar categorias',
        noCategories: 'Nenhuma categoria ainda',
        createCategoriesText: 'Crie sua primeira categoria para organizar os produtos',
        createCategories: 'Criar categoria',
        editTitle: 'Editar categoria',
        categoryRequired: 'O nome da categoria é obrigatório.',
        categoryUpdate: 'Categoria atualizada com sucesso!',
        failedCategoryUpdate: 'Falha ao atualizar categoria',
        failedCategoryCreate: 'Falha ao criar categoria',
        failedCategoryDelete: 'Falha ao excluir categoria',
        createSuccessCategories: 'Categoria criada com sucesso',
        deleteSuccessCategories: 'Categoria excluída com sucesso',
    },

    // ==================== Products Page ====================
    products: {
        title: 'Produtos',
        subtitle: 'Gerenciar produtos da loja',
        newProduct: 'Novo produto',
        thImage: 'Imagem',
        thName: 'Nome',
        thPrice: 'Preço',
        thStock: 'Estoque',
        thCategory: 'Categoria',
        thActive: 'Ativo',
        thActions: 'Ações',
        formName: 'Nome',
        formDescription: 'Descrição',
        formPrice: 'Preço (R$)',
        formStock: 'Quantidade em estoque',
        formImageUrl: 'URL da imagem',
        formCategory: 'Categoria',
        formActive: 'Ativo',
        formDiscount: 'Desconto %',
        formInitialStock: 'Estoque inicial',
        formDealExpires: 'Promoção expira em',
        formFeatured: 'Destaque',
        selectCategory: 'Selecione uma categoria',
        searchProduct: 'Pesquisar produtos...',
        loadingProducts: 'Carregando produtos...',
        deleteProduct: 'Excluir produto',
        deleteProductText: 'Tem certeza de que deseja excluir este produto? Esta ação não pode ser desfeita.',
        thDiscount: 'Desconto',
        thFeatured: 'Destaque',
        featuredYes: 'Sim',
        featuredNo: 'Não'
    },

    // ==================== Products JS ====================
    productsJs: {
        failedProducts: 'Falha ao carregar produtos',
        noProducts: 'Nenhum produto ainda',
        createProductsText: 'Crie seu primeiro produto para começar',
        createProducts: 'Criar produto',
        editTitle: 'Editar produto',
        requiredName: 'O nome do produto é obrigatório.',
        requiredPrice: 'O preço deve ser zero ou um valor positivo.',
        requiredStock: 'A quantidade em estoque deve ser zero ou um valor positivo.',
        requiredDiscount: 'A porcentagem de desconto deve estar entre 0 e 100.',
        requiredInitialStock: 'O estoque inicial deve ser zero ou um valor positivo.',
        selectCategory: 'Por favor, selecione uma categoria.',
        failedProductUpdate: 'Falha ao atualizar produto',
        failedProductCreate: 'Falha ao criar produto',
        failedProductDelete: 'Falha ao excluir produto',
        updateSuccessProduct: 'Produto atualizado com sucesso!',
        createSuccessProducts: 'Produto criado com sucesso',
        deleteSuccessProducts: 'Produto excluído com sucesso',
    },

    // ==================== Users Page ====================
    users: {
        title: 'Usuários',
        subtitle: 'Gerenciar usuários do sistema',
        newUser: 'Novo usuário',
        thName: 'Nome',
        thEmail: 'E-mail',
        thRole: 'Função',
        thPhone: 'Telefone',
        thCreated: 'Criado em',
        thActions: 'Ações',
        formName: 'Nome',
        formEmail: 'E-mail',
        formPassword: 'Senha',
        formRole: 'Função',
        formPhone: 'Telefone',
        formProfileImage: 'URL da imagem de perfil',
        selectRole: 'Selecione uma função',
        searchUser: 'Pesquisar usuários...',
        changePassword: 'Alterar senha',
        newPassword: 'Nova senha',
        loadingUsers: 'Carregando usuários...',
        placeholderEGName: 'ex: João Silva',
        placeholderEGEmail: 'ex: joao@gmail.com',
        enterPass: 'Digite a senha',
        enterPassNew: 'Digite a nova senha',
        deleteUser: 'Excluir usuário',
        deleteUserText: 'Tem certeza de que deseja excluir este usuário? Esta ação não pode ser desfeita.',
        cannotDeleteSelf: 'Você não pode excluir sua própria conta.'
    },

    // ==================== Addresses Page ====================
    addresses: {
        title: 'Endereços',
        subtitle: 'Gerenciar endereços dos usuários',
        newAddress: 'Novo endereço',
        thStreet: 'Rua',
        thNumber: 'Número',
        thCity: 'Cidade',
        thState: 'Estado',
        thUser: 'Usuário',
        thActions: 'Ações',
        formStreet: 'Rua',
        formNumber: 'Número',
        formComplement: 'Complemento',
        formCity: 'Cidade',
        formState: 'Estado',
        formZipCode: 'CEP',
        formCountry: 'País',
        formUser: 'Usuário',
        selectUser: 'Selecione um usuário',
        createTitle: 'Novo endereço',
        deleteAddresses: 'Excluir endereço',
        deleteAddressText: 'Tem certeza de que deseja excluir este endereço? Esta ação não pode ser desfeita.'
    },

    // ==================== Addresses JS ====================
    addressesJs: {
        loading: 'Carregando endereços...',
        failedLoad: 'Falha ao carregar endereços',
        noAddresses: 'Nenhum endereço ainda',
        createFirst: 'Crie seu primeiro endereço para começar',
        createAddressBtn: 'Criar endereço',
        editTitle: 'Editar endereço',
        streetRequired: 'A rua é obrigatória.',
        numberRequired: 'O número é obrigatório.',
        cityRequired: 'A cidade é obrigatória.',
        stateRequired: 'O estado é obrigatório.',
        zipRequired: 'O CEP é obrigatório.',
        countryRequired: 'O país é obrigatório.',
        userRequired: 'Por favor, selecione um usuário.',
        failedUpdate: 'Falha ao atualizar endereço',
        failedCreate: 'Falha ao criar endereço',
        failedDelete: 'Falha ao excluir endereço',
        updateSuccess: 'Endereço atualizado com sucesso',
        createSuccess: 'Endereço criado com sucesso',
        deleteSuccess: 'Endereço excluído com sucesso',
        searchPlaceholder: 'Pesquisar endereços...'
    },

    // ==================== Orders Page ====================
    orders: {
        title: 'Pedidos',
        subtitle: 'Gerenciar pedidos dos clientes',
        newOrder: 'Novo pedido',
        thDate: 'Data',
        thCustomer: 'Cliente',
        thTotal: 'Total',
        thStatus: 'Status',
        thActions: 'Ações',
        formStatus: 'Status',
        formTotal: 'Valor total',
        formUser: 'Usuário',
        formAddress: 'Endereço',
        selectStatus: 'Selecione o status',
        selectUser: 'Selecione um usuário',
        selectAddress: 'Selecione um endereço',
        createTitle: 'Novo pedido',
        searchPlaceholder: 'Pesquisar pedidos...',
        loadingOrders: 'Carregando pedidos...',
        totalAmount: 'Valor total (R$)',
        updateStatus: 'Atualizar status',
        statusPending: 'Pendente',
        statusConfirmed: 'Confirmado',
        statusShipped: 'Enviado',
        statusDelivered: 'Entregue',
        statusCancelled: 'Cancelado',
        deleteConfirmTitle: 'Excluir pedido',
        deleteConfirmText: 'Tem certeza de que deseja excluir este pedido? Isso também excluirá todos os seus itens. Esta ação não pode ser desfeita.'
    },

    // ==================== Orders JS ====================
    ordersJs: {
        failedLoad: 'Falha ao carregar pedidos',
        noOrders: 'Nenhum pedido ainda',
        firstOrderText: 'Crie seu primeiro pedido para começar',
        createOrder: 'Criar pedido',
        clickStatus: 'Clique para alterar o status',
        selectUserPlaceholder: 'Selecione um usuário...',
        selectUserFirst: 'Selecione um usuário primeiro...',
        selectAddressPlaceholder: 'Selecione um endereço...',
        loading: 'Carregando...',
        editTitle: 'Editar pedido',
        noAddresses: 'Nenhum endereço para este usuário',
        failedAddresses: 'Falha ao carregar endereços',
        requiredUser: 'Por favor, selecione um usuário.',
        requiredAddress: 'Por favor, selecione um endereço.',
        requiredStatus: 'Por favor, selecione um status.',
        invalidTotal: 'O valor total deve ser zero ou positivo.',
        updateSuccess: 'Pedido atualizado com sucesso',
        createSuccess: 'Pedido criado com sucesso',
        deleteSuccess: 'Pedido excluído com sucesso',
        statusUpdateSuccess: 'Status atualizado para ',
        failedUpdate: 'Falha ao atualizar pedido',
        failedCreate: 'Falha ao criar pedido',
        failedDelete: 'Falha ao excluir pedido',
        failedStatus: 'Falha ao atualizar status',
        connectionError: 'Erro de conexão',
    },

    // ==================== Order Items Page ====================
    orderItems: {
        title: 'Itens do Pedido',
        subtitle: 'Gerenciar itens dos pedidos',
        newItem: 'Novo item',
        thOrder: 'Pedido',
        thProduct: 'Produto',
        thQuantity: 'Quantidade',
        thPrice: 'Preço',
        thSubtotal: 'Subtotal',
        loadingItems: 'Carregando items...',
        thActions: 'Ações',
        formOrder: 'Pedido',
        formProduct: 'Produto',
        formQuantity: 'Quantidade',
        formPrice: 'Preço unitário',
        formSubtotal: 'Subtotal',
        unitPrice: 'Preço unitário (R$)',
        selectOrder: 'Selecione um pedido',
        selectProduct: 'Selecione um produto',
        createTitle: 'Novo item do pedido',
        deleteItem: 'Excluir item',
        deleteItemText: 'Tem certeza de que deseja excluir este item? Esta ação não pode ser desfeita.'
    },

    // ==================== Order Items JS ====================
    orderItemsJs: {
        failedLoad: 'Falha ao carregar itens',
        noItems: 'Nenhum item ainda',
        firstItemText: 'Adicione itens aos pedidos para começar',
        addItem: 'Adicionar item',
        orderLabel: 'Pedido #',
        selectOrder: 'Selecione um pedido...',
        selectProduct: 'Selecione um produto...',
        searchPlaceholder: 'Pesquisar itens...',
        requiredOrder: 'Por favor, selecione um pedido.',
        requiredProduct: 'Por favor, selecione um produto.',
        requiredQuantity: 'A quantidade deve ser pelo menos 1.',
        invalidPrice: 'O preço deve ser zero ou positivo.',
        updateSuccess: 'Item atualizado com sucesso',
        createSuccess: 'Item criado com sucesso',
        deleteSuccess: 'Item excluído com sucesso',
        failedUpdate: 'Falha ao atualizar item',
        failedCreate: 'Falha ao criar item',
        failedDelete: 'Falha ao excluir item',
        connectionError: 'Erro de conexão',
        createTitle: 'Novo item',
        editTitle: 'Editar item',
        deleteConfirmText: 'Tem certeza de que deseja excluir "{label}"? Esta ação não pode ser desfeita.'
    },

    // ==================== Language Names ====================
    lang: {
        en: 'English',
        pt: 'Português',
        es: 'Español',
        fr: 'Français',
        changed: 'Idioma alterado para'
    },

    // ==================== Settings Page ====================
    settings: {
        title: 'Configurações',
        subtitle: 'Gerencie sua loja, conta e preferências do sistema',

        tabStore: 'Loja',
        tabNotifications: 'Notificações',
        tabAccount: 'Conta',
        tabSecurity: 'Segurança',
        tabDanger: 'Zona de perigo',

        // Store
        storeTitle: 'Informações da loja',
        storeDesc: 'Configure as informações públicas da sua loja e preferências regionais.',
        storeName: 'Nome da loja',
        storeDescription: 'Descrição da loja',
        storeLogo: 'URL do Logotipo',
        currency: 'Moeda',
        timezone: 'Fuso horário',
        storeSaved: 'Configurações da loja salvas com sucesso',
        errStoreName: 'O nome da loja é obrigatório',

        // Notifications
        notifTitle: 'Preferências de notificação',
        notifDesc: 'Escolha quais notificações você deseja receber no painel administrativo.',
        notifDisableAll: 'Desativar todas as notificações',
        notifDisableAllDesc: 'Desliga todos os tipos de notificação de uma vez.',
        notifNewOrders: 'Novos pedidos',
        notifNewOrdersDesc: 'Ser notificado quando um cliente faz um novo pedido.',
        notifNewUsers: 'Novos usuários',
        notifNewUsersDesc: 'Ser notificado quando um novo usuário se cadastrar.',
        notifLowStock: 'Alertas de estoque baixo',
        notifLowStockDesc: 'Alerta quando o estoque de um produto cai abaixo de um limite.',
        notifOrderStatus: 'Alterações no status do pedido',
        notifOrderStatusDesc: 'Receber atualizações quando o status de um pedido for alterado.',
        notifNewReviews: 'Novas avaliações de produtos',
        notifNewReviewsDesc: 'Ser notificado quando os clientes avaliam produtos.',
        notifNewMessages: 'Novas mensagens de chat',
        notifNewMessagesDesc: 'Ser notificado quando um cliente lhe envia uma mensagem.',
        notifSaved: 'Preferências de notificação salvas',

        // Account
        accountTitle: 'Informações da conta',
        accountDesc: 'Atualize suas informações pessoais e foto de perfil.',
        profilePhoto: 'Foto de perfil',
        profilePhotoDesc: 'Adicione uma URL para exibir sua imagem de perfil no painel.',
        accountName: 'Nome completo',
        accountEmail: 'E-mail',
        accountSaved: 'Informações da conta atualizadas',
        errName: 'Nome é obrigatório',
        errEmail: 'E-mail é obrigatório',
        errUpdate: 'Falha ao atualizar',

        // Security
        securityTitle: 'Alterar senha',
        securityDesc: 'Atualize sua senha. Use pelo menos 8 caracteres com uma mistura de letras e números.',
        currentPassword: 'Senha atual',
        newPassword: 'Nova senha',
        confirmPassword: 'Confirmar nova senha',
        updatePassword: 'Atualizar senha',
        passUpdated: 'Senha atualizada com sucesso',
        errCurrentPass: 'Senha atual é obrigatória',
        errNewPassLen: 'A nova senha deve ter pelo menos 8 caracteres',
        errPassMatch: 'A nova senha e a confirmação não coincidem',

        // Danger
        dangerTitle: 'Zona de perigo',
        dangerDesc: 'Estas ações são irreversíveis. Prossiga com cautela.',
        clearOrders: 'Limpar todos os pedidos',
        clearOrdersDesc: 'Excluir permanentemente todos os pedidos e itens de pedido no sistema.',
        clearData: 'Limpar dados',
        resetData: 'Redefinir dados de teste',
        resetDataDesc: 'Excluir todos os produtos, categorias e pedidos. Os usuários padrão permanecerão.',
        resetButton: 'Redefinir',
        deleteAccount: 'Excluir minha conta',
        deleteAccountDesc: 'Remover sua conta de administrador permanentemente. Você será desconectado.',
        typeConfirm: 'Digite CONFIRMAR para prosseguir:',
        errTypeConfirm: 'Você deve digitar CONFIRMAR exatamente',
        confirmClearOrders: 'Limpar todos os pedidos?',
        confirmClearOrdersText: 'Isso excluirá permanentemente todos os pedidos e itens. Esta ação não pode ser desfeita.',
        confirmResetData: 'Redefinir todos os dados de teste?',
        confirmResetDataText: 'Isso excluirá todos os pedidos, produtos e categorias. Usuários permanecerão. Esta ação não pode ser desfeita.',
        confirmDeleteAccount: 'Excluir sua conta?',
        confirmDeleteAccountText: 'Sua conta de administrador será removida permanentemente e você será desconectado. Esta ação não pode ser desfeita.',
        ordersCleared: 'Todos os pedidos foram limpos',
        dataReset: 'Dados de teste foram redefinidos',
        accountDeleted: 'Conta excluída. Desconectando...'
    },

    // ==================== Profile Page ====================
    profile: {
        tabAccount: 'Conta',
        tabSecurity: 'Segurança',

        accountTitle: 'Informações da conta',
        accountDesc: 'Atualize suas informações pessoais e foto de perfil.',
        profilePhoto: 'Foto de perfil',
        profilePhotoDesc: 'Adicione uma URL para exibir sua imagem de perfil no painel.',
        accountName: 'Nome completo',
        accountEmail: 'E-mail',
        accountPhone: 'Telefone',
        accountSaved: 'Informações da conta atualizadas',
        errName: 'Nome é obrigatório',
        errEmail: 'E-mail é obrigatório',
        errUpdate: 'Falha ao atualizar',
        repeat: 'Repetir nova senha',
        least: 'Pelo menos 8 caracteres',

        securityTitle: 'Alterar senha',
        securityDesc: 'Atualize sua senha. Use pelo menos 8 caracteres com uma mistura de letras e números.',
        currentPassword: 'Senha atual',
        newPassword: 'Nova senha',
        confirmPassword: 'Confirmar nova senha',
        updatePassword: 'Atualizar senha',
        passUpdated: 'Senha atualizada com sucesso',
        errCurrentPass: 'Senha atual é obrigatória',
        errNewPassLen: 'A nova senha deve ter pelo menos 8 caracteres',
        errPassMatch: 'A nova senha e a confirmação não coincidem'
    },

    // ==================== Register ====================
    register: {
        title: 'Crie sua conta',
        subtitle: 'Junte-se ao SmartOrder e comece a comprar',
        name: 'Nome Completo',
        namePlaceholder: 'Digite seu nome completo',
        email: 'E-mail',
        emailPlaceholder: 'Digite seu e-mail',
        password: 'Senha',
        passwordPlaceholder: 'Crie uma senha',
        confirmPassword: 'Confirmar Senha',
        confirmPasswordPlaceholder: 'Confirme sua senha',
        submit: 'Criar Conta',
        hasAccount: 'Já tem uma conta?',
        login: 'Entrar',
    },

    // ==================== Register JS ====================
    registerJs: {
        nameRequired: 'O nome completo é obrigatório.',
        emailInvalid: 'Digite um e-mail válido.',
        emailTaken: 'Este e-mail já está em uso.',
        passwordShort: 'A senha deve ter pelo menos 6 caracteres.',
        passwordMismatch: 'As senhas não coincidem.',
        invalidData: 'Dados inválidos. Verifique os campos.',
        server: 'Erro interno. Por favor, tente novamente.',
        connection: 'Erro de conexão. Por favor, tente novamente.'
    },

    // ==================== Common ====================
    common: {
        save: 'Salvar',
        cancel: 'Cancelar',
        delete: 'Excluir',
        edit: 'Editar',
        create: 'Criar',
        close: 'Fechar',
        confirm: 'Confirmar',
        yes: 'Sim',
        no: 'Não',
        loading: 'Carregando...',
        error: 'Erro',
        success: 'Sucesso',
        unknown: 'Desconhecido',
        new: 'Novo',
        search: 'Pesquisar...',
        footer: 'SmartOrder API v1.0 — Painel administrativo',
        noResults: 'Nenhum resultado encontrado',
        confirmDelete: 'Tem certeza que deseja excluir',
        confirmDeleteText: 'Esta ação não pode ser desfeita.',
        deleteSuccess: 'excluído com sucesso!',
        createSuccess: 'criado com sucesso!',
        updateSuccess: 'atualizado com sucesso!',
        errorLoad: 'Falha ao carregar dados',
        actions: 'Ações',
        showing: 'Mostrando',
        of: 'de',
        results: 'resultados',
        errorConnect: 'Erro de conexão',
        sure: 'Tem certeza de que deseja excluir',
        undone: 'Esta ação não pode ser desfeita',
        admin: 'Administrador',
        customer: 'Cliente',
        saveChanges: 'Salvar alterações',
        connectionError: 'Erro de conexão. Por favor, tente novamente.',
    }

});