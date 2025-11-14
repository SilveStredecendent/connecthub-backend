-- 1. USERS
INSERT INTO users (id, role, name, email, password, cpf, phone, enrollmentid, xp, level, avatar_url, is_active, created_at, updated_at) VALUES
(1, 'STUDENT', 'João Silva', 'joao.silva@estudante.edu.br', '$2a$10$hashedpassword1', '12345678901', '11987654321', 202401, 150.0, 2, 'https://avatar.com/joao', true, '2024-01-15 08:00:00', '2024-01-15 08:00:00'),
(2, 'TEACHER', 'Prof. Maria Santos', 'maria.santos@professor.edu.br', '$2a$10$hashedpassword2', '98765432109', '11912345678', NULL, 500.0, 5, 'https://avatar.com/maria', true, '2024-01-10 09:00:00', '2024-01-10 09:00:00'),
(3, 'STUDENT', 'Carlos Oliveira', 'carlos.oliveira@estudante.edu.br', '$2a$10$hashedpassword3', '45678912301', '11976543210', 202402, 75.0, 1, 'https://avatar.com/carlos', true, '2024-01-20 10:00:00', '2024-01-20 10:00:00'),
(4, 'STUDENT', 'Ana Costa', 'ana.costa@estudante.edu.br', '$2a$10$hashedpassword4', '78912345602', '11965432109', 202403, 300.0, 3, 'https://avatar.com/ana', true, '2024-01-12 11:00:00', '2024-01-12 11:00:00'),
(5, 'ADMIN', 'Admin Sistema', 'admin@sistema.edu.br', '$2a$10$hashedpassword5', '32165498703', '11954321098', NULL, 1000.0, 10, 'https://avatar.com/admin', true, '2024-01-01 12:00:00', '2024-01-01 12:00:00'),
(6, 'STUDENT', 'Fernanda Lima', 'fernanda.lima@estudante.edu.br', '$2a$10$hashedpassword6', '11122233344', '11944445555', 202404, 50.0, 1, 'https://avatar.com/fernanda', true, '2024-01-25 13:00:00', '2024-01-25 13:00:00'),
(7, 'TEACHER', 'Prof. Ricardo Mendes', 'ricardo.mendes@professor.edu.br', '$2a$10$hashedpassword7', '55566677788', '11933332222', NULL, 650.0, 7, 'https://avatar.com/ricardo', true, '2024-01-05 14:00:00', '2024-01-05 14:00:00'),
(8, 'STUDENT', 'Guilherme Rocha', 'guilherme.rocha@estudante.edu.br', '$2a$10$hashedpassword8', '99988877766', '11922221111', 202405, 120.0, 2, 'https://avatar.com/guilherme', true, '2024-02-01 15:00:00', '2024-02-01 15:00:00'),
(9, 'STUDENT', 'Isabela Gomes', 'isabela.gomes@estudante.edu.br', '$2a$10$hashedpassword9', '44455566677', '11911110000', 202406, 210.0, 3, 'https://avatar.com/isabela', true, '2024-02-05 16:00:00', '2024-02-05 16:00:00'),
(10, 'ADMIN', 'Gerente Financeiro', 'gerente@sistema.edu.br', '$2a$10$hashedpassword10', '00011122233', '11900009999', NULL, 1200.0, 12, 'https://avatar.com/gerente', true, '2023-12-01 17:00:00', '2023-12-01 17:00:00'),
(11, 'TEACHER', 'Prof. Laura Bessa', 'laura.bessa@professor.edu.br', '$2a$10$hashedpassword12', '33322211100', '11977776666', NULL, 450.0, 5, 'https://avatar.com/laura', true, '2024-01-01 19:00:00', '2024-01-01 19:00:00'),
(12, 'STUDENT', 'Lucas Pereira', 'lucas.pereira@estudante.edu.br', '$2a$10$hashedpassword14', '22233344455', '11955554444', 202409, 400.0, 4, 'https://avatar.com/lucas', false, '2024-02-20 21:00:00', '2024-02-20 21:00:00'),
(13, 'TEACHER', 'Prof. Bruno Oliveira', 'bruno.oliveira@professor.edu.br', '$2a$10$hashedpassword15', '77766655544', '11944443333', NULL, 800.0, 8, 'https://avatar.com/bruno', true, '2023-11-15 22:00:00', '2023-11-15 22:00:00'),
(14, 'STUDENT', 'Sofia Batista', 'sofia.batista@estudante.edu.br', '$2a$10$hashedpassword16', '10120230340', '11933334444', 202410, 50.0, 1, 'https://avatar.com/sofia', true, '2024-03-01 07:30:00', '2024-03-01 07:30:00'),
(15, 'ADMIN', 'Supervisor RH', 'supervisor.rh@sistema.edu.br', '$2a$10$hashedpassword17', '54321098765', '11922223333', NULL, 900e0, 9, 'https://avatar.com/supervisor', false, '2024-03-05 08:30:00', '2024-03-05 08:30:00');
SELECT setval('users_seq', (SELECT MAX(id) FROM users), true);

-- 2. COURSES
INSERT INTO courses (id, name, code, semester, workload, is_active, created_at, updated_at) VALUES
(1, 'Programação Orientada a Objetos', 'POO001', 3, 80, true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(2, 'Estruturas de Dados', 'ED002', 4, 60, true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(3, 'Banco de Dados', 'BD003', 5, 100, true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(4, 'Desenvolvimento Web', 'WEB004', 6, 120, true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(5, 'Inteligência Artificial', 'IA005', 8, 90, true, '2024-01-01 08:00:00', '2024-01-01 08:00:00');
SELECT setval('courses_seq', (SELECT MAX(id) FROM courses), true);

-- 3. BADGES
INSERT INTO badges (id, name, description, image_url, criteria, is_active, created_at, updated_at) VALUES
(1, 'Primeiro Post', 'Parabéns por fazer sua primeira pergunta na plataforma!', 'https://badges.com/first-post.png', 'Criar o primeiro tópico', true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(2, 'Solucionador', 'Você ajudou outros estudantes com suas respostas!', 'https://badges.com/solver.png', 'Ter 5 respostas marcadas como solução', true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(3, 'Expert', 'Reconhecido pela comunidade como especialista na área', 'https://badges.com/expert.png', 'Atingir 500 pontos de XP', true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(4, 'Colaborativo', 'Ativo na comunidade, sempre disposto a ajudar', 'https://badges.com/collaborative.png', 'Responder 20 perguntas', true, '2024-01-01 08:00:00', '2024-01-01 08:00:00'),
(5, 'Mentor', 'Guia e orienta novos estudantes na plataforma', 'https://badges.com/mentor.png', 'Atingir nível 5 e ter 10 soluções aceitas', true, '2024-01-01 08:00:00', '2024-01-01 08:00:00');
SELECT setval('badges_seq', (SELECT MAX(id) FROM badges), true);

-- 4. PROJECTS
INSERT INTO projects (id, name, description, author_id, repository_url, image_url, count_views, is_active, created_at, updated_at) VALUES
(1, 'Sistema de Biblioteca', 'Sistema completo para gerenciamento de biblioteca universitária com empréstimos e devoluções', 1, 'https://github.com/joao/biblioteca-sistema', 'https://images.com/biblioteca.png', 120, true, '2024-01-25 14:00:00', '2024-01-25 14:00:00'),
(2, 'E-commerce Responsivo', 'Loja virtual desenvolvida com React e Node.js, incluindo carrinho e pagamentos', 4, 'https://github.com/ana/ecommerce-app', 'https://images.com/ecommerce.png', 200, true, '2024-02-01 16:00:00', '2024-02-01 16:00:00'),
(3, 'API de Gerenciamento de Tarefas', 'REST API para aplicativo de produtividade com autenticação JWT', 3, 'https://github.com/carlos/task-api', 'https://images.com/tasks.png', 85, true, '2024-02-10 10:00:00', '2024-02-10 10:00:00'),
(4, 'Dashboard Analytics', 'Dashboard interativo para visualização de dados com gráficos e relatórios', 2, 'https://github.com/maria/analytics-dashboard', 'https://images.com/dashboard.png', 150, true, '2024-02-15 09:00:00', '2024-02-15 09:00:00'),
(5, 'App Mobile Fitness', 'Aplicativo React Native para acompanhamento de exercícios e dieta', 1, 'https://github.com/joao/fitness-app', 'https://images.com/fitness.png', 90, true, '2024-02-18 11:00:00', '2024-02-18 11:00:00');
SELECT setval('projects_seq', (SELECT MAX(id) FROM projects), true);


-- 5. TOPICS
INSERT INTO topics (id, title, content, course_id, author_id, topic_status, count_views, is_active, created_at, updated_at) VALUES
(1, 'Dúvida sobre Herança em Java', 'Estou com dificuldades para entender como funciona a herança em Java. Alguém pode me explicar com exemplos práticos?', 1, 1, 'NOT_ANSWERED', 25, true, '2024-02-01 09:00:00', '2024-02-01 09:00:00'),
(2, 'Implementação de Lista Ligada', 'Preciso de ajuda para implementar uma lista ligada em C++. Qual seria a melhor abordagem?', 2, 3, 'NOT_SOLVED', 15, true, '2024-02-05 10:30:00', '2024-02-05 10:30:00'),
(3, 'Normalização de Banco de Dados', 'Como aplicar corretamente a terceira forma normal em um banco de dados?', 3, 4, 'SOLVED', 40, true, '2024-02-10 14:00:00', '2024-02-10 14:00:00'),
(4, 'React vs Vue.js', 'Qual framework JavaScript vocês recomendam para iniciantes? React ou Vue.js?', 4, 1, 'NOT_SOLVED', 60, true, '2024-02-15 16:20:00', '2024-02-15 16:20:00'),
(5, 'Algoritmos de Machine Learning', 'Quais são os algoritmos mais importantes para começar a estudar ML?', 5, 4, 'NOT_ANSWERED', 35, true, '2024-02-20 11:45:00', '2024-02-20 11:45:00');
SELECT setval('topics_seq', (SELECT MAX(id) FROM topics), true);



-- 6. ANSWERS
INSERT INTO answers (id, content, topic_id, author_id, count_likes, count_dislikes, is_solution, is_active, created_at, updated_at) VALUES
(1, 'Herança é um dos pilares da POO. Basicamente permite que uma classe filha herde atributos e métodos de uma classe pai. Por exemplo: class Pessoa { String nome; } class Estudante extends Pessoa { String curso; }', 1, 2, 5, 0, false, true, '2024-02-01 10:00:00', '2024-02-01 10:00:00'),
(2, 'Para lista ligada, você precisa criar uma estrutura Node com dados e ponteiro para o próximo. Depois implementar métodos insert, delete e search. Posso compartilhar um código exemplo se quiser.', 2, 2, 8, 0, true, true, '2024-02-05 11:00:00', '2024-02-05 11:00:00'),
(3, 'A 3FN elimina dependências transitivas. Se A->B e B->C, então C deve estar em outra tabela. Exemplo: se Estudante->Curso e Curso->Departamento, então Departamento deve ter sua própria tabela.', 3, 2, 12, 1, true, true, '2024-02-10 15:00:00', '2024-02-10 15:00:00'),
(4, 'Para iniciantes, recomendo React. Tem mais documentação, comunidade maior e mais oportunidades de trabalho. Vue é mais fácil de aprender, mas React te dará uma base sólida.', 4, 4, 15, 2, false, true, '2024-02-15 17:00:00', '2024-02-15 17:00:00'),
(5, 'Comece com regressão linear, árvores de decisão e k-means. São algoritmos fundamentais e relativamente simples de entender. Depois parta para redes neurais.', 5, 2, 3, 0, false, true, '2024-02-20 12:30:00', '2024-02-20 12:30:00');
SELECT setval('answers_seq', (SELECT MAX(id) FROM answers), true);


-- 7. USER_BADGES
INSERT INTO user_badges (id, user_id, badge_id, earned_at, is_active, created_at, updated_at) VALUES
(1, 1, 1, '2024-02-01 09:05:00', true, '2024-02-01 09:05:00', '2024-02-01 09:05:00'),
(2, 2, 3, '2024-01-15 14:00:00', true, '2024-01-15 14:00:00', '2024-01-15 14:00:00'),
(3, 2, 5, '2024-01-20 16:30:00', true, '2024-01-20 16:30:00', '2024-01-20 16:30:00'),
(4, 4, 1, '2024-02-10 14:05:00', true, '2024-02-10 14:05:00', '2024-02-10 14:05:00'),
(5, 4, 2, '2024-02-15 18:00:00', true, '2024-02-15 18:00:00', '2024-02-15 18:00:00');
SELECT setval('user_badges_seq', (SELECT MAX(id) FROM user_badges), true);


-- 8. XP_TRANSACTIONS
INSERT INTO xp_transactions (id, user_id, amount, description, reference_id, type, is_active, created_at, updated_at) VALUES
(1, 1, 10, 'Primeira pergunta postada', 1, 'TOPIC_CREATED', true, '2024-02-01 09:00:00', '2024-02-01 09:00:00'),
(2, 2, 15, 'Resposta útil postada', 1, 'ANSWER_POSTED', true, '2024-02-01 10:30:00', '2024-02-01 10:30:00'),
(3, 3, 10, 'Nova pergunta sobre estruturas de dados', 2, 'TOPIC_CREATED', true, '2024-02-05 10:30:00', '2024-02-05 10:30:00'),
(4, 2, 25, 'Resposta marcada como melhor resposta', 3, 'BEST_ANSWER', true, '2024-02-05 11:30:00', '2024-02-05 11:30:00'),
(5, 1, 20, 'Projeto publicado', 1, 'PROJECT_PUBLISHED', true, '2024-01-25 14:30:00', '2024-01-25 14:30:00');
SELECT setval('xp_transactions_seq', (SELECT MAX(id) FROM xp_transactions), true);


-- 9. NOTIFICATIONS
INSERT INTO notifications (id, user_id, type, title, message, reference_id, is_read, is_active, created_at, updated_at) VALUES
(1, 1, 'TOPIC_ANSWERED', 'Nova resposta!', 'Sua pergunta sobre herança em Java recebeu uma resposta.', 1, false, true, '2024-02-01 10:05:00', '2024-02-01 10:05:00'),
(2, 2, 'BEST_ANSWER_SELECTED', 'Resposta selecionada!', 'Sua resposta foi escolhida como a melhor resposta.', 3, true, true, '2024-02-10 16:00:00', '2024-02-10 16:00:00'),
(3, 3, 'TOPIC_ANSWERED', 'Resposta na sua pergunta', 'Alguém respondeu sua dúvida sobre lista ligada.', 2, false, true, '2024-02-05 11:05:00', '2024-02-05 11:05:00'),
(4, 4, 'BADGE_EARNED', 'Nova conquista!', 'Você ganhou uma nova medalha!', 2, false, true, '2024-02-15 18:05:00', '2024-02-15 18:05:00'),
(5, 1, 'LEVEL_UP', 'Parabéns!', 'Você subiu de nível!', 1, true, true, '2024-02-16 09:00:00', '2024-02-16 09:00:00');
SELECT setval('notifications_seq', (SELECT MAX(id) FROM notifications), true);


-- 11. PROJECT_COMMENTS
INSERT INTO project_comments (id, text, project_id, author_id, is_active, created_at, updated_at) VALUES
(1, 'Projeto muito bem estruturado! O código está bem organizado e documentado. Parabéns!', 1, 4, true, '2024-01-26 15:00:00', '2024-01-26 15:00:00'),
(2, 'Gostei muito da interface do e-commerce. Como você implementou o sistema de pagamentos?', 2, 3, true, '2024-02-02 17:30:00', '2024-02-02 17:30:00'),
(3, 'A API está muito bem documentada. Seria possível adicionar testes unitários?', 3, 2, true, '2024-02-11 11:30:00', '2024-02-11 11:30:00'),
(4, 'Dashboard ficou incrível! Os gráficos estão muito informativos. Qual biblioteca usou?', 4, 1, true, '2024-02-16 10:15:00', '2024-02-16 10:15:00'),
(5, 'App de fitness tem um design muito clean. Pretende adicionar integração com wearables?', 5, 4, true, '2024-02-19 12:45:00', '2024-02-19 12:45:00');
SELECT setval('project_comments_seq', (SELECT MAX(id) FROM project_comments), true);

