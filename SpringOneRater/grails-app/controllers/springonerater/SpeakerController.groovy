package springonerater

import org.springframework.dao.DataIntegrityViolationException

class SpeakerController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	static defaultAction = "index"

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [speakerInstanceList: Speaker.list(params), speakerInstanceTotal: Speaker.count()]
    }

    def create() {
        [speakerInstance: new Speaker(params)]
    }

    def save() {
        def speakerInstance = new Speaker(params)
        if (!speakerInstance.save(flush: true)) {
            render(view: "create", model: [speakerInstance: speakerInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'speaker.label', default: 'Speaker'), speakerInstance.id])
        redirect(action: "show", id: speakerInstance.id)
    }

    def show() {
        def speakerInstance = Speaker.get(params.id)
        if (!speakerInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'speaker.label', default: 'Speaker'), params.id])
            redirect(action: "list")
            return
        }

        [speakerInstance: speakerInstance]
    }

    def edit() {
        def speakerInstance = Speaker.get(params.id)
        if (!speakerInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'speaker.label', default: 'Speaker'), params.id])
            redirect(action: "list")
            return
        }

        [speakerInstance: speakerInstance]
    }

    def update() {
        def speakerInstance = Speaker.get(params.id)
        if (!speakerInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'speaker.label', default: 'Speaker'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (speakerInstance.version > version) {
                speakerInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'speaker.label', default: 'Speaker')] as Object[],
                          "Another user has updated this Speaker while you were editing")
                render(view: "edit", model: [speakerInstance: speakerInstance])
                return
            }
        }

        speakerInstance.properties = params

        if (!speakerInstance.save(flush: true)) {
            render(view: "edit", model: [speakerInstance: speakerInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'speaker.label', default: 'Speaker'), speakerInstance.id])
        redirect(action: "show", id: speakerInstance.id)
    }

    def delete() {
        def speakerInstance = Speaker.get(params.id)
        if (!speakerInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'speaker.label', default: 'Speaker'), params.id])
            redirect(action: "list")
            return
        }

        try {
            speakerInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'speaker.label', default: 'Speaker'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'speaker.label', default: 'Speaker'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
