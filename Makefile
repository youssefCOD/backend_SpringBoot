main:
	mvn spring-boot:run
git:
	git add .
	git commit -m "$(m)"
	git push