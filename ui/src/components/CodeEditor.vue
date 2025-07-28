<template>
  <div class="code-editor" :class="[themeClass, { 'read-only': readonly }]">
    <div class="editor-container">
      <!-- 行号区域 -->
      <div class="line-numbers" ref="lineNumbersRef">
        <div
          v-for="lineNumber in lineCount"
          :key="lineNumber"
          class="line-number"
          :class="{ 'current-line': currentLine === lineNumber }"
        >
          {{ lineNumber }}
        </div>
      </div>

      <!-- 代码编辑区域 -->
      <textarea
        ref="textareaRef"
        v-model="localValue"
        :readonly="readonly"
        :placeholder="placeholder"
        class="editor-textarea"
        @scroll="handleScroll"
        @input="handleInput"
        @keydown="handleKeydown"
        @click="updateCurrentLine"
        @keyup="updateCurrentLine"
        @focus="updateCurrentLine"
      ></textarea>
    </div>

    <!-- 状态栏 -->
    <div class="editor-statusbar" v-if="showStatusBar">
      <div class="status-item">
        <span>行: {{ currentLine }}</span>
      </div>
      <div class="status-item">
        <span>列: {{ currentColumn }}</span>
      </div>
      <div class="status-item">
        <span>总行数: {{ lineCount }}</span>
      </div>
      <div class="status-item">
        <span>字符数: {{ characterCount }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'

// 定义props
const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  readonly: {
    type: Boolean,
    default: false
  },
  placeholder: {
    type: String,
    default: ''
  },
  theme: {
    type: String,
    default: 'light' // light, dark, code-editor, text-editor
  },
  showStatusBar: {
    type: Boolean,
    default: true
  }
})

// 定义emits
const emit = defineEmits(['update:modelValue'])

// 响应式数据
const localValue = ref(props.modelValue || '')
const textareaRef = ref(null)
const lineNumbersRef = ref(null)
const currentLine = ref(1)
const currentColumn = ref(1)

// 计算属性
const lineCount = computed(() => {
  if (!localValue.value) return 1
  return localValue.value.split('\n').length || 1
})

const characterCount = computed(() => {
  return localValue.value ? localValue.value.length : 0
})

const themeClass = computed(() => {
  return `theme-${props.theme}`
})

// 处理滚动同步
const handleScroll = () => {
  if (textareaRef.value && lineNumbersRef.value) {
    lineNumbersRef.value.scrollTop = textareaRef.value.scrollTop
  }
  updateCurrentLine()
}

// 处理输入
const handleInput = () => {
  emit('update:modelValue', localValue.value)
}

// 处理键盘事件
const handleKeydown = (event) => {
  if (props.readonly) return

  // Tab键处理
  if (event.key === 'Tab') {
    event.preventDefault()
    const start = textareaRef.value.selectionStart
    const end = textareaRef.value.selectionEnd

    // 插入制表符
    localValue.value = localValue.value.substring(0, start) + '\t' + localValue.value.substring(end)

    // 恢复光标位置
    setTimeout(() => {
      if (textareaRef.value) {
        textareaRef.value.selectionStart = textareaRef.value.selectionEnd = start + 1
      }
    }, 0)
  }
}

// 更新当前光标位置
const updateCurrentLine = () => {
  if (!textareaRef.value) return

  const textarea = textareaRef.value
  const text = textarea.value
  const cursorPos = textarea.selectionStart

  // 计算光标所在的行号和列号
  const textBeforeCursor = text.substring(0, cursorPos)
  const lines = textBeforeCursor.split('\n')
  const lineNumber = lines.length
  const columnNumber = lines[lines.length - 1].length + 1

  currentLine.value = lineNumber
  currentColumn.value = columnNumber
}

// 复制到剪贴板
const copyToClipboard = () => {
  if (localValue.value) {
    navigator.clipboard.writeText(localValue.value).then(() => {
      // 成功回调可以通过事件传递给父组件
    }).catch(() => {
      // 失败回调可以通过事件传递给父组件
    })
  }
}

// 格式化内容
const formatContent = () => {
  if (!localValue.value || props.readonly) return

  try {
    // 这里可以添加更复杂的格式化逻辑
    const formatted = localValue.value
      .split('\n')
      .map(line => line.trimEnd())
      .join('\n')
      .replace(/\n{3,}/g, '\n\n') // 最多保留两个空行

    localValue.value = formatted
  } catch (error) {
    // 错误处理可以通过事件传递给父组件
  }
}

// 监听外部值变化
watch(() => props.modelValue, (newValue) => {
  if (newValue !== localValue.value) {
    localValue.value = newValue || ''
  }
})

// 组件挂载后初始化
onMounted(() => {
  if (textareaRef.value) {
    textareaRef.value.addEventListener('scroll', handleScroll)
    // 初始化光标位置
    updateCurrentLine()
  }
})

// 暴露方法给父组件
defineExpose({
  copyToClipboard,
  formatContent
})
</script>

<style scoped>
.code-editor {
  width: 100%;
  height: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  position: relative;
}

/* 浅色主题 */
.code-editor.theme-light {
  background-color: #ffffff;
}

.code-editor.theme-light .editor-container {
  background-color: #ffffff;
}

.code-editor.theme-light .line-numbers {
  background-color: #f8f9fa;
  border-right: 1px solid #e4e7ed;
  color: #909399;
}

.code-editor.theme-light .line-number.current-line {
  background-color: #e6f7ff;
  color: #409eff;
  font-weight: bold;
}

.code-editor.theme-light .editor-textarea {
  background-color: #ffffff;
  color: #333333;
}

.code-editor.theme-light .editor-textarea:read-only {
  background-color: #f5f7fa;
  color: #666666;
}

/* 深色主题 */
.code-editor.theme-dark {
  background-color: #1e1e1e;
  border: 1px solid #3c3c3c;
}

.code-editor.theme-dark .editor-container {
  background-color: #1e1e1e;
}

.code-editor.theme-dark .line-numbers {
  background-color: #252526;
  border-right: 1px solid #3c3c3c;
  color: #858585;
}

.code-editor.theme-dark .line-number.current-line {
  background-color: #264f78;
  color: #ffffff;
  font-weight: bold;
}

.code-editor.theme-dark .editor-textarea {
  background-color: #1e1e1e;
  color: #d4d4d4;
}

.code-editor.theme-dark .editor-textarea:read-only {
  background-color: #2d2d30;
  color: #a0a0a0;
}

/* 代码编辑器主题 */
.code-editor.theme-code-editor {
  background-color: #1e1e1e;
  border: 1px solid #3c3c3c;
}

.code-editor.theme-code-editor .editor-container {
  background-color: #1e1e1e;
}

.code-editor.theme-code-editor .line-numbers {
  background-color: #252526;
  border-right: 1px solid #3c3c3c;
  color: #5a5a5a;
}

.code-editor.theme-code-editor .line-number.current-line {
  background-color: #264f78;
  color: #c5c5c5;
  font-weight: bold;
}

.code-editor.theme-code-editor .editor-textarea {
  background-color: #1e1e1e;
  color: #d4d4d4;
  font-family: 'Consolas', 'Courier New', monospace;
}

.code-editor.theme-code-editor .editor-textarea:read-only {
  background-color: #2d2d30;
  color: #a0a0a0;
}

/* 文本编辑器主题 */
.code-editor.theme-text-editor {
  background-color: #ffffff;
  border: 1px solid #dcdfe6;
}

.code-editor.theme-text-editor .editor-container {
  background-color: #ffffff;
}

.code-editor.theme-text-editor .line-numbers {
  background-color: #f0f0f0;
  border-right: 1px solid #dcdfe6;
  color: #888888;
}

.code-editor.theme-text-editor .line-number.current-line {
  background-color: #fffacd;
  color: #333333;
  font-weight: bold;
}

.code-editor.theme-text-editor .editor-textarea {
  background-color: #ffffff;
  color: #333333;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.code-editor.theme-text-editor .editor-textarea:read-only {
  background-color: #f8f8f8;
  color: #666666;
}

/* 只读模式 */
.code-editor.read-only {
  opacity: 0.9;
}

/* 编辑器容器 */
.editor-container {
  display: flex;
  height: 100%;
  position: relative;
  flex: 1;
  min-height: 0;
}

/* 行号区域 */
.line-numbers {
  width: 50px;
  padding: 10px 5px;
  text-align: right;
  overflow: hidden;
  flex-shrink: 0;
  font-family: 'Courier New', Courier, monospace;
  font-size: 14px;
  line-height: 1.5;
  user-select: none;
}

.line-number {
  padding: 0 5px;
}

/* 编辑区域 */
.editor-textarea {
  flex: 1;
  border: none;
  outline: none;
  resize: none;
  font-size: 14px;
  line-height: 1.5;
  padding: 10px;
  background-color: transparent;
}

.editor-textarea:focus {
  outline: none;
}

/* 状态栏 */
.editor-statusbar {
  display: flex;
  padding: 4px 8px;
  border-top: 1px solid #e4e7ed;
  background-color: #f5f7fa;
  font-size: 12px;
  color: #606266;
}

.code-editor.theme-dark .editor-statusbar {
  background-color: #2d2d30;
  border-top: 1px solid #3c3c3c;
  color: #cccccc;
}

.code-editor.theme-code-editor .editor-statusbar {
  background-color: #252526;
  border-top: 1px solid #3c3c3c;
  color: #cccccc;
}

.code-editor.theme-text-editor .editor-statusbar {
  background-color: #f8f8f8;
  border-top: 1px solid #e4e7ed;
  color: #606266;
}

.status-item {
  margin-right: 15px;
  white-space: nowrap;
}

/* 滚动条样式 */
.line-numbers::-webkit-scrollbar {
  width: 0px;
}

.editor-textarea::-webkit-scrollbar {
  width: 8px;
}

.editor-textarea::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.code-editor.theme-dark .editor-textarea::-webkit-scrollbar-track,
.code-editor.theme-code-editor .editor-textarea::-webkit-scrollbar-track {
  background: #2d2d30;
}

.editor-textarea::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.code-editor.theme-dark .editor-textarea::-webkit-scrollbar-thumb,
.code-editor.theme-code-editor .editor-textarea::-webkit-scrollbar-thumb {
  background: #555555;
}

.editor-textarea::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.code-editor.theme-dark .editor-textarea::-webkit-scrollbar-thumb:hover,
.code-editor.theme-code-editor .editor-textarea::-webkit-scrollbar-thumb:hover {
  background: #777777;
}
</style>