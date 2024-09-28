CREATE TABLE IF NOT EXISTS users
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(45) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    bio VARCHAR(1000),
    image VARCHAR(500),
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_follows
(
    follower_user_id INT NOT NULL,
    followed_user_id INT NOT NULL,
    PRIMARY KEY (follower_user_id, followed_user_id),
    CONSTRAINT fk_followed_user FOREIGN KEY (followed_user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_follower_user FOREIGN KEY (follower_user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS articles
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    author_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL,
    description VARCHAR(500) NOT NULL,
    body VARCHAR(10000) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_author FOREIGN KEY (author_id) REFERENCES users(id),
    CONSTRAINT UQ_author_slug UNIQUE (author_id, slug)
);

CREATE TABLE IF NOT EXISTS tags
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    value VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS article_tags
(
    article_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (article_id, tag_id),
    CONSTRAINT fk_article FOREIGN KEY (article_id) REFERENCES articles (id) ON DELETE CASCADE,
    CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS article_favorites
(
    article_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (article_id, user_id),
    CONSTRAINT fk_favorited_article FOREIGN KEY (article_id) REFERENCES articles (id) ON DELETE CASCADE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    id INT PRIMARY KEY,
    author_id INT NOT NULL,
    article_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    body VARCHAR(800) NOT NULL,
    CONSTRAINT fk_comment_article FOREIGN KEY (article_id) REFERENCES articles (id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
);