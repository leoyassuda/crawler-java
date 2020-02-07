# Crawler

Um simples crawler para pegar os top 3 mais votados no site do Reddit e salvar em um arquivo TXT.

## Instruções

realizar o clone do repositório
```bash
git clone https://gitlab.com/leoyassuda/crawler-java.git
``` 

### Pré-requisitos

> - Java 1.8
> - Maven 3
> - Docker 


### Compilação e geração do projeto

Acessar a pasta em que foi feito o clone e executar o seguinte comando:

> pasta_projeto_clonado/crawlers/

```bash
mvn clean install -DskipTests
```

## Executando os testes

A implementar.

### Executando a aplicação

Por padrão será executado na porta 8181 e exposta pela mesma no Docker.

Após a execução o projeto, poderá ser inserido via terminal os parâmetros do subreddit e separados por ponto e virgula.

> Realizar o build do Docker e executar 
> - **Build:** `docker build --file=Dockerfile --tag=crawler-reddit:latest --rm=true .` 
> - **Run:** `docker run -i -t --name=app-crawler-reddit --publish=8181:8181 --volume=app-crawler-reddit:/var/lib/crawler crawler-reddit:latest` 
>
 
### Telegram Bot

> Mandar mensagem para o username **@YassudaCrawlerBot**
> ##### Comandos:
> - **start:** `/start` - mensagem inicial com as instruções de uso. 
> - **search:** `/search [subreddits]` - comando que executo o extrator de dados do reddit. O parâmetro será 
passado sem colchetes e separados por ponto e virgula. Por exemplo: /search cats;brazil;worldnews 
> 

## Construído com

* [Spring-boot](https://projects.spring.io/spring-boot/) - Framework
* [Maven](https://maven.apache.org/) - Dependency management and automation of compilation and tests 
* [Docker](https://www.docker.com/) - Software containerization platform 

## Autor

* **Leo Yassuda** - *Initial work* - [Job-Backend-Developer](https://github.com/leoyassuda/desafios) - site [www.leoyas.com](www.leoyas.com)
