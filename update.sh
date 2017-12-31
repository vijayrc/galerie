echo "$(tput setaf 1)galerie: update started$(tput sgr0)"
sed -i 's/false/true/' config.properties

/home/vijayrc/tools/gradle-1.6/bin/gradle fatJar
echo "$(tput setaf 1)galerie: fatjar done!$(tput sgr0)"

cp layer/build/libs/layer-1.0.jar ~/tools/galerie/layer.jar
cp -r layer/src/main/resources/static ~/tools/galerie/
cp config.properties ~/tools/galerie/
echo "$(tput setaf 1)galerie: copied files(tput sgr0)"

echo "$(tput setaf 1)galerie: update ended$(tput sgr0)"
