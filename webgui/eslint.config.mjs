import eslint from '@eslint/js';
import tseslint from 'typescript-eslint';
import angular from 'angular-eslint';
import importPlugin from 'eslint-plugin-import';
import importAlias from '@dword-design/eslint-plugin-import-alias';


export default tseslint.config(
  {
    files: ['**/*.ts'],
    extends: [
      eslint.configs.recommended,
      ...tseslint.configs.recommended,
      ...tseslint.configs.stylistic,
      ...angular.configs.tsRecommended,
      importAlias.configs.recommended,
    ],
    plugins: {
      import: importPlugin,
    },
    processor: angular.processInlineTemplates,
    rules: {
      '@angular-eslint/directive-selector': [
        'error',
        {
          type: 'attribute',
          prefix: 'app',
          style: 'camelCase',
        },
      ],
      '@angular-eslint/component-selector': [
        'error',
        {
          type: 'element',
          prefix: 'app',
          style: 'kebab-case',
        },
      ],
      indent: ['error', 2],
      quotes: ['error', 'single', { avoidEscape: true }],
      semi: ['error', 'always'],
      'lines-between-class-members': ['error', 'always', { exceptAfterSingleLine: true }],
      'no-multiple-empty-lines': ['error', { max: 1, maxBOF: 0, maxEOF: 0 }],
      'padded-blocks': ['error', { classes: 'never' }],
      'import/order': [
        'error',
        {
          groups: ['builtin', 'external', 'internal', 'parent', 'sibling', 'index'],
          'newlines-between': 'always',
          alphabetize: { order: 'asc', caseInsensitive: true },
        },
      ],
      'import/newline-after-import': ['error', { count: 1 }],
      'import/no-default-export': 'error',
      '@dword-design/import-alias/prefer-alias': [
        'error',
        {
          'alias': {
            '~pages': './src/app/pages',
            '~auth': './src/app/modules/auth',
            '~common': './src/app/modules/common',
            '~fuel-order': './src/app/modules/fuel-order',
            '~fuel-station': './src/app/modules/fuel-station',
            '~manager': './src/app/modules/manager'
          }
        }
      ]
    },
  },
  {
    files: ['**/*.html'],
    extends: [
      ...angular.configs.templateRecommended,
      ...angular.configs.templateAccessibility,
    ],
  }
);
