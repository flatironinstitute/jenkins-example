String call(String tag = "latest") {
  def name = env.JOB_NAME.toLowerCase().replaceAll('%2f', '/')
  return "$REGISTRY_PREFIX/$name:$tag"
}
