export const DEPLOY_DOMAIN = import.meta.env.VITE_DEPLOY_DOMAIN || 'http://localhost'
export const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8088/api'
export const STATIC_BASE_URL = `${API_BASE_URL}/static`

export const getDeployUrl = (deployKey: string) => `${DEPLOY_DOMAIN}/${deployKey}`

export const getStaticPreviewUrl = (codeGenType: string, appId: string) =>
  `${STATIC_BASE_URL}/${codeGenType}_${appId}/`
