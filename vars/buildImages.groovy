def call(Map args, List<Map> images) {
  podTemplate(inheritFrom: 'podman', showRawYaml: false) {
    node(POD_LABEL) {
      if (args.checkout != false) {
        checkout scm
      }
      container('main') {
        images.each { img ->
          def context    = img.context ?: ".";
          def dockerfile = img.dockerfile ?: "Dockerfile";
          def buildArgs  = img.buildArgs ?: "";
          def prep       = img.prep;
          String image = imageName(img.tag ?: "latest")

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

