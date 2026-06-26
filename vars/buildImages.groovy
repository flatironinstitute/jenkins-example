def call(Map args, List<Map> images) {
  podTemplate(inheritFrom: 'podman', showRawYaml: false) {
    node(POD_LABEL) {
      if (args.checkout != false) {
        checkout scm
      }
      container('main') {
        images.each { image ->
          def context    = image.context ?: ".";
          def dockerfile = image.dockerfile ?: "Dockerfile";
          def buildArgs  = image.buildArgs ?: "";
          def prep       = image.prep;
          String image = imageName(image.tag ?: "latest")

          if (prep) {
            def extra = prep.call()
            if (extra in String)
              buildArgs += " $extra"
          }
          sh "podman build -t $image -f $context/$dockerfile $buildArgs $context"
          sh "podman push $image"
        }
      }
    }
  }
}

