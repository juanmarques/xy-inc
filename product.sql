DROP TABLE IF EXISTS `products`;

CREATE TABLE `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) DEFAULT NULL,
  `description` blob,
  `price` decimal DEFAULT NULL,
  `category` varchar(500) DEFAULT NULL,
  `picture` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
LOCK TABLES `products` WRITE;


INSERT INTO `products` VALUES (1,'Console Playstation 4 500GB Nacional - CUH-1214A B01',
'O PlayStation�4 � o melhor lugar para jogar jogos din�micos e conectados, com gr�fico rico e alta velocidade, personaliza��o inteligente, funcionalidades sociais altamente integradas e recursos de segunda tela inovadores. Combinando conte�do sem igual, experi�ncias de jogo envolventes, todos os seus aplicativos favoritos de entretenimento digital e as exclusividades do PlayStation, o PS4 � centrado nos jogadores, permitindo-lhes que joguem quando, onde e como quiserem. O PS4 permite que os melhores desenvolvedores de jogos do mundo liberem sua criatividade e estendam os limites do jogo por meio de um sistema que est� sintonizado especificamente com suas necessidades.','235282','Gamer','playstation4.jpg');

commit;
