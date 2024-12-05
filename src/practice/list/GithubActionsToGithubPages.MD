# Автоматизация деплоя на github pages 

Это практическая работа является следствием  [предыдущей практической работы](ReactGithubPages). На прошлом занятии необходимо было настроить развертывание приложения на github pages. Теперь хочется сделать этот процесс автоматическим

Задачи на пару:
- Создать пайплайн, который устанавливает зависимости для React-приложения и делает build версию
- Доработать пайплайн до функциональности, при которой React-приложение автоматически разворачивается на Github Pages с приминением Github Actions.
- Настроить deploy по [триггеру вручную](https://docs.github.com/en/actions/using-workflows/manually-running-a-workflow)
- Настроить автоматический прогон линтера в пайплайне. [Настройка линтера](https://www.freecodecamp.org/news/how-to-add-eslint-to-your-react-project/)

### Методические материалы
- https://dev.to/dyarleniber/setting-up-a-ci-cd-workflow-on-github-actions-for-a-react-app-with-github-pages-and-codecov-4hnp

Нужно создать файл конфигурации в репозитории и разместить его в .github/workflows/<filename>.yaml

Необходимая конфигурация для Github Actions:

```yaml
name: Deploy

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:

    runs-on: ubuntu-latest

    strategy:
      matrix:
        node-version: [20.x]

    steps:
    - name: Checkout repository
      uses: actions/checkout@v4

    - name: Set up Node.js ${{ matrix.node-version }}
      uses: actions/setup-node@v1
      with:
        node-version: ${{ matrix.node-version }}

    - name: Install dependencies
      run: npm install

    - name: Build
      run: npm run build

    - name: Deploy
      run: |
        git config --global user.name $user_name
        git config --global user.email $user_email
        git remote set-url origin https://${github_token}@github.com/${repository}
        npm run deploy
      env:
        user_name: 'github-actions[bot]'
        user_email: 'github-actions[bot]@users.noreply.github.com'
        github_token: ${{ secrets.ACTIONS_DEPLOY_ACCESS_TOKEN }}
        repository: ${{ github.repository }}
```

Не забыть добавить токен в secrets

### Дополнительный материал для чтения
- https://github.com/features/actions
- https://habr.com/ru/articles/711278/