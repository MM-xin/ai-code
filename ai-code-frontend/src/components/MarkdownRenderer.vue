<template>
  <div class="markdown-content" v-html="renderedMarkdown"></div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import MarkdownIt from 'markdown-it'
import hljs from 'highlight.js'
import 'highlight.js/styles/github.css'

interface Props {
  content: string
}

const props = defineProps<Props>()

const md: MarkdownIt = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  highlight: function (str: string, lang: string): string {
    if (lang && hljs.getLanguage(lang)) {
      try {
        return (
          '<pre class="hljs"><code>' +
          hljs.highlight(str, { language: lang, ignoreIllegals: true }).value +
          '</code></pre>'
        )
      } catch {}
    }
    return '<pre class="hljs"><code>' + md.utils.escapeHtml(str) + '</code></pre>'
  },
})

const renderedMarkdown = computed(() => md.render(props.content))
</script>

<style scoped>
.markdown-content { line-height: 1.6; color: #333; word-wrap: break-word; }
.markdown-content :deep(h1), .markdown-content :deep(h2), .markdown-content :deep(h3) { margin: 1.2em 0 0.5em; font-weight: 600; }
.markdown-content :deep(p) { margin: 0.8em 0; }
.markdown-content :deep(ul), .markdown-content :deep(ol) { margin: 0.8em 0; padding-left: 1.5em; }
.markdown-content :deep(code) { background: #f1f1f1; padding: 0.2em 0.4em; border-radius: 3px; font-size: 0.9em; }
.markdown-content :deep(pre) { background: #f8f8f8; border: 1px solid #e1e1e1; border-radius: 6px; padding: 1em; overflow-x: auto; margin: 1em 0; }
.markdown-content :deep(pre code) { background: transparent; padding: 0; }
.markdown-content :deep(a) { color: #1890ff; text-decoration: none; }
.markdown-content :deep(a:hover) { text-decoration: underline; }
</style>
